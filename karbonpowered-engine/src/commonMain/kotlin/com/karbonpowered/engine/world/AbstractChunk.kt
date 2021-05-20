package com.karbonpowered.engine.world

import com.karbonpowered.engine.util.BitSize

abstract class AbstractChunk {
    companion object {
        /**
         * Stores the size of the amount of blocks in this Chunk
         */
        val BLOCKS = BitSize(4)
    }
}