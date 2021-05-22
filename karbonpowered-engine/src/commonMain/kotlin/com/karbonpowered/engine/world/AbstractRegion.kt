package com.karbonpowered.engine.world

import com.karbonpowered.engine.util.BitSize

abstract class AbstractRegion {
    abstract fun getChunk(x: Int, y: Int, z: Int, loadOption: LoadOption = LoadOption.LOAD_GEN): AbstractChunk

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
