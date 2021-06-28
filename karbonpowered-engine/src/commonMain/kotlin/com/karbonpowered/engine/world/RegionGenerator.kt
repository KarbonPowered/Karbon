package com.karbonpowered.engine.world

import com.karbonpowered.engine.util.collection.map.palette.AtomicPaletteIntStore
import com.karbonpowered.engine.util.cuboid.CuboidIntBuffer
import com.karbonpowered.math.multiplyToShift
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RegionGenerator(
    val region: KarbonRegion
) : CoroutineScope {
    override val coroutineContext: CoroutineContext =
        CoroutineName(toString()) + Dispatchers.Default

    private val width: Int = 1 // TODO: extend buffering size for generating neighbour chunks
    private val sections = KarbonRegion.CHUNKS.SIZE / width
    private val shift = width.multiplyToShift()
    private val mask = width - 1

    private val generatedChunks = Array(sections) { x ->
        Array(sections) { y ->
            Array(sections) { z ->
                async(start = CoroutineStart.LAZY) {
                    val chunkX = region.chunkX + x
                    val chunkY = region.chunkY + y
                    val chunkZ = region.chunkZ + z

                    val buffer = CuboidIntBuffer(
                        baseX = chunkX shl KarbonChunk.BLOCKS.BITS,
                        baseY = chunkY shl KarbonChunk.BLOCKS.BITS,
                        baseZ = chunkZ shl KarbonChunk.BLOCKS.BITS,
                        sizeX = KarbonChunk.BLOCKS.SIZE shl shift,
                        sizeY = KarbonChunk.BLOCKS.SIZE shl shift,
                        sizeZ = KarbonChunk.BLOCKS.SIZE shl shift
                    )

                    val world = requireNotNull(region.world.refresh(region.engine.worldManager))
                    world.generator.generate(buffer, world)

                    val chunkBuffer = buffer // TODO: Copy the buffer depending on the generation width
                    val blockStore = AtomicPaletteIntStore(KarbonChunk.BLOCKS.BITS, true, 10, chunkBuffer.data)

                    KarbonChunk(region, chunkX, chunkY, chunkZ, blockStore)
                }
            }
        }
    }

    suspend fun generateChunk(chunkXWorld: Int, chunkYWorld: Int, chunkZWorld: Int): KarbonChunk {
        val chunkXLocal = chunkXWorld and mask.inv() and KarbonRegion.CHUNKS.MASK
        val chunkYLocal = chunkYWorld and mask.inv() and KarbonRegion.CHUNKS.MASK
        val chunkZLocal = chunkZWorld and mask.inv() and KarbonRegion.CHUNKS.MASK
        return generatedChunks[chunkXLocal][chunkYLocal][chunkZLocal].await()
    }

    override fun toString(): String = "RegionGenerator($region)"
}
