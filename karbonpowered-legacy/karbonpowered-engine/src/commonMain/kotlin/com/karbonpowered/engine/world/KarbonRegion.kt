package com.karbonpowered.engine.world

import com.karbonpowered.engine.entity.EntityManager
import com.karbonpowered.engine.scheduler.AsyncManager
import com.karbonpowered.engine.snapshot.SnapshotManager
import com.karbonpowered.engine.util.BitSize
import com.karbonpowered.engine.world.cuboid.Cube
import com.karbonpowered.engine.world.discrete.Position
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

private const val FILE_EXISTS = false

/**
 * Represents a cube containing 16x16x16 Chunks (256x256x256 Blocks)
 */
@OptIn(ExperimentalTime::class)
class KarbonRegion(
    world: KarbonWorld,
    val regionX: Int,
    val regionY: Int,
    val regionZ: Int,
    val regionSource: RegionSource
) : AsyncManager, Cube(Position(world, regionX, regionY, regionZ), BLOCKS.SIZE.toFloat()) {
    val engine = world.engine
    val regionGenerator = RegionGenerator(this)
    val snapshotManager = SnapshotManager()
    val entityManager = EntityManager(this)
    val blockX = regionX shl BLOCKS.BITS
    val blockY = regionY shl BLOCKS.BITS
    val blockZ = regionZ shl BLOCKS.BITS
    val chunkX = regionX shl CHUNKS.BITS
    val chunkY = regionY shl CHUNKS.BITS
    val chunkZ = regionZ shl CHUNKS.BITS
    val isLoaded: Boolean = true

    /**
     * Chunks used for ticking.
     */
    private val chunks = atomic(Array<KarbonChunk?>(CHUNKS.VOLUME) { null })

    /**
     * All live chunks. These are not ticked, but can be accessed.
     */
    private val live = atomic(Array<KarbonChunk?>(CHUNKS.VOLUME) { null })

    suspend fun chunk(x: Int, y: Int, z: Int, loadOption: LoadOption): KarbonChunk? {
        val localX = x and CHUNKS.MASK
        val localY = y and CHUNKS.MASK
        val localZ = z and CHUNKS.MASK
        val chunk = chunks.value[getChunkKey(localX, localY, localZ)]

        if (chunk != null) {
            return chunk
        }

        if (!loadOption.isLoad) {
            return null
        }

        return loadOrGenChunk(x, y, z, loadOption)
    }

    private suspend fun loadOrGenChunk(x: Int, y: Int, z: Int, loadOption: LoadOption): KarbonChunk? {
        val localX = x and CHUNKS.MASK
        val localY = y and CHUNKS.MASK
        val localZ = z and CHUNKS.MASK

        val newChunk = if (loadOption.isLoad) loadChunk(localX, localY, localZ) else null
        if (newChunk != null || !loadOption.isGenerate) {
            return newChunk
        }

        val chunk = regionGenerator.generateChunk(x, y, z)
        setChunk(chunk, x, y, z)
        return chunk
    }

    // TODO: Loading chunk
    private fun loadChunk(localX: Int, localY: Int, localZ: Int): KarbonChunk? {
        return null
    }

    fun setChunk(newChunk: KarbonChunk, x: Int, y: Int, z: Int) {
        val chunkIndex = getChunkKey(x, y, z)
        val liveChunks = live.value
        val newChunks = liveChunks.copyOf()
        newChunks[chunkIndex] = newChunk
        live.value = newChunks
        if (chunks.value[chunkIndex] == null) {
            chunks.value[chunkIndex] = newChunk
        }
    }

    fun setGeneratedChunks(newChunks: Array<Array<Array<KarbonChunk>>>) {
        while (true) {
            val liveChunks = live.value
            val newArray = liveChunks.copyOf()
            val width = newChunks.size
            repeat(width) { x ->
                repeat(width) { y ->
                    repeat(width) { z ->
                        val currentChunk = newChunks[x][y][z]
                        val chunkKey = getChunkKey(x, y, z)
                        check(liveChunks[chunkKey] == null) {
                            "Tried to set a generated chunk, but a chunk already existed!"
                        }
                        newArray[chunkKey] = currentChunk
                    }
                }
            }
            if (live.compareAndSet(liveChunks, newArray)) {
                break
            }
        }
    }

    override suspend fun startTickRun(stage: Int, duration: Duration) {
        when (stage) {
            0 -> updateEntities(duration)
            1 -> updateDynamics(duration)
        }
    }

    private suspend fun updateEntities(duration: Duration) = coroutineScope {
        entityManager.entities.values.forEach { entity ->
            launch {
                entity.tick(duration)
            }
        }
    }

    private suspend fun updateDynamics(duration: Duration) = coroutineScope {
        launch {
            entityManager.entities.values.forEach { entity ->
                launch {
                    entity.physics.onPrePhysicsTick()
                }
            }
        }.join()
        //  TODO: update physics engine
        launch {
            entityManager.entities.values.forEach { entity ->
                launch {
                    entity.physics.onPostPhysicsTick()
                }
            }
        }.join()
    }

    override suspend fun finalizeRun() {
        entityManager.finalizeRun()
    }

    override suspend fun preSnapshotRun() {
        entityManager.preSnapshotRun()
    }

    override fun copySnapshotRun() {
        chunks.value = live.value
        snapshotManager.copyAllSnapshots()
        entityManager.copyAllSnapshots()
    }

    override fun toString(): String = "Region(${world.get()}, $regionX, $regionY, $regionZ)"

    companion object {
        /**
         * Stores the size of the amount of chunks in this Region
         */
        val CHUNKS = BitSize(4)

        /**
         * Stores the size of the amount of blocks in this Region
         */
        val BLOCKS = BitSize(CHUNKS.BITS + KarbonChunk.BLOCKS.BITS)

        fun getChunkKey(chunkX: Int, chunkY: Int, chunkZ: Int): Int {
            val cx = chunkX and CHUNKS.MASK
            val cy = chunkY and CHUNKS.MASK
            val cz = chunkZ and CHUNKS.MASK
            return CHUNKS.AREA * cx + CHUNKS.SIZE * cy + cz
        }
    }
}
