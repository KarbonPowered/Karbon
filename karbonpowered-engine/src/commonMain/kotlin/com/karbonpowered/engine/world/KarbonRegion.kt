package com.karbonpowered.engine.world

import com.karbonpowered.engine.entity.EntityManager
import com.karbonpowered.engine.scheduler.AsyncManager
import com.karbonpowered.engine.snapshot.SnapshotManager
import com.karbonpowered.engine.util.BitSize
import com.karbonpowered.engine.util.collection.map.palette.AtomicPaletteIntStore
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic

private const val FILE_EXISTS = false

/**
 * Represents a cube containing 16x16x16 Chunks (256x256x256 Blocks)
 */
class KarbonRegion(
    val world: KarbonWorld,
    val x: Int,
    val y: Int,
    val z: Int,
    val regionSource: RegionSource
) : AsyncManager {
    val snapshotManager = SnapshotManager()
    val entityManager = EntityManager(this)
    val blockX = x shl BLOCKS.BITS
    val blockY = y shl BLOCKS.BITS
    val blockZ = z shl BLOCKS.BITS
    val chunkX = x shl CHUNKS.BITS
    val chunkY = y shl CHUNKS.BITS
    val chunkZ = z shl CHUNKS.BITS

    private val generator = RegionGenerator(this)
    private val chunks: Array<Array<Array<AtomicRef<KarbonChunk?>>>> =
        Array(CHUNKS.SIZE) { Array(CHUNKS.SIZE) { Array(CHUNKS.SIZE) { atomic(null) } } }
    private val neighbours = Array(3) { dx ->
        Array(3) { dy ->
            Array(3) { dz ->
                atomic(world.getRegion(x + dx - 1, y + dy - 1, z + dz - 1, LoadOption.NO_LOAD))
            }
        }
    }

    private var activeChunks = atomic(0)

    fun unlinkNeighbours() {
        repeat(3) { dx ->
            repeat(3) { dy ->
                repeat(3) { dz ->
                    val region = world.getRegion(x + dx - 1, y + dy - 1, z + dz - 1, LoadOption.NO_LOAD)
                    region?.unlinkNeighbour(this)
                }
            }
        }
    }

    private fun unlinkNeighbour(region: KarbonRegion) {
        repeat(3) { dx ->
            repeat(3) { dy ->
                repeat(3) { dz ->
                    neighbours[dx][dy][dz].compareAndSet(region, null)
                }
            }
        }
    }

    fun getLocalRegion(dx: Int, dy: Int, dz: Int, loadOption: LoadOption = LoadOption.NO_LOAD): KarbonRegion? {
        val ref = neighbours[dx][dy][dy]
        var region = ref.value
        if (region == null) {
            region = world.getRegion(x + dx - 1, y + dy - 1, z + dz - 1, loadOption)
            ref.compareAndSet(null, region)
        }
        return region
    }

    suspend fun getChunk(x: Int, y: Int, z: Int, loadOption: LoadOption): KarbonChunk? {
        val cx = x and CHUNKS.MASK
        val cy = y and CHUNKS.MASK
        val cz = z and CHUNKS.MASK

        var chunk = chunks[cx][cy][cz].value
        if (chunk != null) {
            checkChunkLoaded(chunk, loadOption)
            return chunk
        }

        val fileExists = FILE_EXISTS

        // TODO: loading chunks if file exists
        if (loadOption.isLoad && fileExists) {
            chunk = KarbonChunk(this, chunkX + x, chunkY + y, chunkZ + z, AtomicPaletteIntStore(0, false))
        }

        if (loadOption.isGenerate && !fileExists && chunk == null) {
            chunk = generator.generateChunk(chunkX + x, chunkY + y, chunkZ + z)
            if (chunk != null) {
                chunks[x][y][z].value = chunk
                checkChunkLoaded(chunk, loadOption)
                return chunk
            } else {
                println("Chunk generation failed! Region $this, chunk ($x, $y, $z): $loadOption")
                Throwable().printStackTrace()
            }
        }

        if (chunk == null) {
            return null
        }

        chunk = setChunk(chunk, x, y, z, false)
        checkChunkLoaded(chunk, loadOption)
        return chunk
    }

    private fun setChunk(newChunk: KarbonChunk, x: Int, y: Int, z: Int, generated: Boolean): KarbonChunk {
        val ref = chunks[x][y][z]
        while (true) {
            if (ref.compareAndSet(null, newChunk)) {
                if (generated) {
                    // TODO: queue lightning
                    // TODO: queue chunk sending to players
                }
                activeChunks.incrementAndGet()

                // TODO: add loaded entities
                // TODO: add block updates

                // TODO: call ChunkLoadEvent

                return newChunk
            }

            val oldChunk = ref.value
            if (oldChunk != null) {
                // TODO: unload chunk data
                return oldChunk
            }
        }
    }

    fun removeChunk(chunk: KarbonChunk): Boolean {
        if (chunk.region != this) {
            return false
        }

        val currentRef = chunks[chunk.x and CHUNKS.MASK][chunk.y and CHUNKS.MASK][chunk.z and CHUNKS.MASK]
        val currentChunk = currentRef.value
        if (currentChunk != chunk) {
            return false
        }
        if (currentRef.compareAndSet(currentChunk, null)) {
            val currentActiveChunks = activeChunks.decrementAndGet()
            // TODO: remove entities
            // TODO: unload chunk data
            // TODO: remove chunk updates

            if (currentActiveChunks == 0) {
                return true
            }
            check(currentActiveChunks >= 0) {
                "Region $this has less than 0 active chunks: $currentActiveChunks"
            }
        }
        return false
    }

    private fun checkChunkLoaded(chunk: KarbonChunk, loadOption: LoadOption) =
        check(!loadOption.isLoad || chunk.cancelUnload()) {
            "Returned unloaded chunk"
        }

    override suspend fun preSnapshotRun() {
        entityManager.preSnapshotRun()
        entityManager.syncEntities()
    }

    override fun copySnapshotRun() {
        entityManager.copyAllSnapshots()
        snapshotManager.copyAllSnapshots()
    }

    companion object {
        /**
         * Stores the size of the amount of chunks in this Region
         */
        val CHUNKS = BitSize(4)

        /**
         * Stores the size of the amount of blocks in this Region
         */
        val BLOCKS = BitSize(CHUNKS.BITS + KarbonChunk.BLOCKS.BITS)
    }
}
