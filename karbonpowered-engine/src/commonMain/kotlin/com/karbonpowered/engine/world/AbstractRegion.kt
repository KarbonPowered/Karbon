package com.karbonpowered.engine.world

import com.karbonpowered.engine.util.BitSize

abstract class AbstractRegion {
    companion object {
        /**
         * Stores the size of the amount of chunks in this Region
         */
        val CHUNKS = BitSize(4)

        /**
         * Stores the size of the amount of blocks in this Region
         */
        val BLOCKS = BitSize(CHUNKS.BITS + AbstractChunk.BLOCKS.BITS)
    }
}