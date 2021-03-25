package com.karbonpowered.engine.world

import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.world.chunk.Chunk
import com.karbonpowered.engine.util.BitSize
import com.karbonpowered.math.vector.IntVector3
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic

class KarbonChunk(
    val world: KarbonWorld,
    val x: Int,
    val y: Int,
    val z: Int
) : Chunk<KarbonChunk> {
    companion object {
        val BLOCKS = BitSize(4)
    }

    protected val saveState = atomic(SaveState.NONE)

    override val isEmpty: Boolean
        get() = TODO("Not yet implemented")
    override val chunkPosition: IntVector3
        get() = TODO("Not yet implemented")

    override fun setBlock(x: Int, y: Int, z: Int, blockState: BlockState): Boolean {
        return true
    }

    override fun removeBlock(x: Int, y: Int, z: Int): Boolean {
        return true
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

    fun cancelUnload() = SaveState.cancelUnload(saveState)

    enum class SaveState {
        UNLOAD_SAVE,
        UNLOAD,
        SAVE,
        NONE,
        SAVING,
        POST_SAVED,
        UNLOADED;

        companion object {
            fun cancelUnload(saveState: AtomicRef<SaveState>): Boolean {
                var success = false
                var oldState: SaveState? = null
                while (!success) {
                    oldState = saveState.value
                    val nextState = when (oldState) {
                        UNLOAD_SAVE -> SAVE
                        UNLOAD -> NONE
                        SAVE -> SAVE
                        NONE -> NONE
                        SAVING -> NONE
                        POST_SAVED -> NONE
                        UNLOADED -> UNLOADED
                    }
                    success = saveState.compareAndSet(oldState, nextState)
                }
                return oldState != UNLOADED
            }
        }
    }
}