package com.karbonpowered.engine.world

import com.karbonpowered.common.concurrent.NamedReentrantLock
import com.karbonpowered.engine.util.collection.map.palette.AtomicPaletteIntStore
import com.karbonpowered.engine.util.cuboid.CuboidIntBuffer
import com.karbonpowered.math.multiplyToShift
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RegionGenerator(
    val region: KarbonRegion,
    val width: Int = 4
) : CoroutineScope {
    override val coroutineContext: CoroutineContext =
        CoroutineName(toString()) + Dispatchers.Default

    private val sections = KarbonRegion.CHUNKS.SIZE / width
    private val generatedChunks = Array(sections) { Array(sections) { Array(sections) { atomic(GenerateState.NONE) } } }
    private val sectionLocks =
        Array(sections) { x -> Array(sections) { Array(sections) { z -> NamedReentrantLock("($x, $z), $region") } } }
    private val shift = width.multiplyToShift()
    private val mask = width - 1
    private val baseChunkX = region.chunkX
    private val baseChunkY = region.chunkX
    private val baseChunkZ = region.chunkX

    fun generateChunk(chunkXWorld: Int, chunkYWorld: Int, chunkZWorld: Int) = launch {
        // Represent the coords of the section of the region
        // Values are from 0 to width
        val sectionX = chunkXWorld and KarbonRegion.CHUNKS.MASK shr shift
        val sectionY = chunkYWorld and KarbonRegion.CHUNKS.MASK shr shift
        val sectionZ = chunkZWorld and KarbonRegion.CHUNKS.MASK shr shift

        // Represent the local chunk coords of the base of the section
        // Values start at 0 and are spaced by width chunks
        val chunkXLocal = chunkXWorld and mask.inv() and KarbonRegion.CHUNKS.MASK
        val chunkYLocal = chunkYWorld and mask.inv() and KarbonRegion.CHUNKS.MASK
        val chunkZLocal = chunkZWorld and mask.inv() and KarbonRegion.CHUNKS.MASK

        val generated = generatedChunks[sectionX][sectionY][sectionZ]
        if (generated.value.isDone) {
            return@launch
        }

        val sectionLock = sectionLocks[sectionX][sectionY][sectionZ]
        if (!sectionLock.lock.tryLock()) {
            return@launch
        }

        try {
            if (generated.value.isDone) {
                return@launch
            }

            var generationIndex = generationCounter.getAndIncrement()
            while (generationIndex == -1) {
                region.engine.info("Ran out of generation index ids, starting again")
                generationIndex = generationCounter.getAndIncrement()
            }

            check(generated.compareAndSet(GenerateState.NONE, GenerateState.IN_PROGRESS)) {
                "Unable to set generate state for column $sectionX, $sectionY, $sectionZ in region${region.base} to in progress state is ${generated.value}"
            }

            try {
                val world = requireNotNull(region.world.refresh(region.engine.worldManager))
                var chunkInWorldX = baseChunkX + chunkXLocal
                var chunkInWorldY = baseChunkY + chunkYLocal
                var chunkInWorldZ = baseChunkZ + chunkZLocal

                val buffer = CuboidIntBuffer(
                    baseX = chunkInWorldX shl KarbonChunk.BLOCKS.BITS,
                    baseY = chunkInWorldY shl KarbonChunk.BLOCKS.BITS,
                    baseZ = chunkInWorldZ shl KarbonChunk.BLOCKS.BITS,
                    sizeX = KarbonChunk.BLOCKS.SIZE shl shift,
                    sizeY = KarbonChunk.BLOCKS.SIZE shl shift,
                    sizeZ = KarbonChunk.BLOCKS.SIZE shl shift
                )
                world.generator.generateChunk(buffer, world)

                val chunks = Array(width) { xx ->
                    chunkInWorldX = baseChunkX + chunkXLocal + xx
                    Array(width) { zz ->
                        chunkInWorldZ = baseChunkZ + chunkZLocal + zz
                        Array(width) { yy ->
                            chunkInWorldY = baseChunkY + chunkYLocal + yy
                            val chunkBuffer = CuboidIntBuffer(
                                baseX = chunkInWorldX shl KarbonChunk.BLOCKS.BITS,
                                baseY = chunkInWorldY shl KarbonChunk.BLOCKS.BITS,
                                baseZ = chunkInWorldZ shl KarbonChunk.BLOCKS.BITS,
                                sizeX = KarbonChunk.BLOCKS.SIZE,
                                sizeY = KarbonChunk.BLOCKS.SIZE,
                                sizeZ = KarbonChunk.BLOCKS.SIZE
                            )
                            chunkBuffer.copyFrom(buffer)
                            KarbonChunk(
                                region,
                                chunkInWorldX,
                                chunkInWorldY,
                                chunkInWorldZ,
                                AtomicPaletteIntStore(KarbonChunk.BLOCKS.BITS, true, 10, chunkBuffer.data)
                            )
                        }
                    }
                }

                check(generated.compareAndSet(GenerateState.IN_PROGRESS, GenerateState.COPYING)) {
                    "Unable to set generate state for column $sectionX, $sectionY, $sectionZ in region${region.base} to in progress state is ${generated.value}"
                }

                region.setGeneratedChunks(chunks)

                // We need to set the generated state before we unlock the readLock so waiting generators get the state immediately
                check(generated.compareAndSet(GenerateState.COPYING, GenerateState.COPIED)) {
                    "Unable to set generate state for column $sectionX, $sectionY, $sectionZ in region${region.base} to in progress state is ${generated.value}"
                }
            } catch (e: Exception) {
                generated.value = GenerateState.NONE
                throw e
            }
        } finally {
            sectionLock.lock.unlock()
        }
    }

    override fun toString(): String = "RegionGenerator($region)"

    enum class GenerateState {
        NONE,
        IN_PROGRESS,
        COPYING,
        COPIED;

        val isDone get() = this == COPIED
        val isInProgress get() = this == IN_PROGRESS || this == COPYING
    }

    companion object {
        private val generationCounter = atomic(1)
    }
}
