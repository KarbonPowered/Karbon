package com.karbonpowered.engine.world

import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.fluid.FluidState
import com.karbonpowered.api.world.WorldLoadOption
import com.karbonpowered.api.world.chunk.ProtoChunk
import com.karbonpowered.api.world.region.Region
import com.karbonpowered.engine.scheduler.TickManager
import com.karbonpowered.engine.util.BitSize
import com.karbonpowered.logging.Logger
import com.karbonpowered.math.vector.IntVector3
import kotlinx.atomicfu.atomic

class KarbonRegion(
    val world: KarbonWorld,
    val x: Int,
    val y: Int,
    val z: Int
) : Region, TickManager {
    companion object {
        val CHUNKS = BitSize(4)
        val BLOCKS = BitSize(CHUNKS.bits + KarbonChunk.BLOCKS.bits)
    }

    val chunks = Array(16) { dx ->
        Array(16) { dy ->
            Array(16) { dz ->
                atomic<KarbonChunk?>(null)
            }
        }
    }
    val neighbours = Array(16) { dx ->
        Array(16) { dy ->
            Array(16) { dz ->
                atomic(world.region(x + dx - 1, y + dy - 1, z + dz - 1, WorldLoadOption.NO_LOAD))
            }
        }
    }

    override fun chunk(x: Int, y: Int, z: Int, loadOption: WorldLoadOption): ProtoChunk<*>? {
        val dx = x and CHUNKS.mask
        val dy = y and CHUNKS.mask
        val dz = z and CHUNKS.mask
        val chunk = chunks[dx][dy][dz].value
        if (chunk != null) {
            checkChunkLoaded(chunk, loadOption)
            return chunk
        }

        val fileExists = false // TODO: Loading chunk from file
        if (loadOption.load && fileExists) {
            TODO("Not yet implemented")
        }

        if (loadOption.generate && !fileExists) {
            generateChunk(x, y, z)
            val generatedChunk = chunks[x][y][z].value
            if (generatedChunk != null) {
                checkChunkLoaded(generatedChunk, loadOption)
                return generatedChunk
            } else {
                Logger.error("Chunk failed to generate! ($loadOption)")
                Logger.info("Region $this")
            }
        }

        return null
    }

    override fun chunkAtBlock(x: Int, y: Int, z: Int, loadOption: WorldLoadOption): ProtoChunk<*>? {
        TODO("Not yet implemented")
    }

    override fun block(x: Int, y: Int, z: Int): BlockState {
        TODO("Not yet implemented")
    }

    override fun fluid(x: Int, y: Int, z: Int): FluidState {
        TODO("Not yet implemented")
    }

    override fun highestYAt(x: Int, z: Int): Int {
        TODO("Not yet implemented")
    }

    private fun generateChunk(x: Int, y: Int, z: Int) {
        chunks[x][y][z].getAndSet(KarbonChunk(world, x, y, z))
    }

    override val blockMin: IntVector3
        get() = TODO("Not yet implemented")
    override val blockMax: IntVector3
        get() = TODO("Not yet implemented")
    override val blockSize: IntVector3
        get() = TODO("Not yet implemented")

    override fun containsBlock(x: Int, y: Int, z: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAreaAvailable(x: Int, y: Int, z: Int): Boolean {
        TODO("Not yet implemented")
    }

    private fun checkChunkLoaded(chunk: KarbonChunk, loadOption: WorldLoadOption) =
            check(!loadOption.load || chunk.cancelUnload()) { "Unloaded chunk returned" }
}