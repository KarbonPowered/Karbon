package com.karbonpowered.engine.world

import com.karbonpowered.engine.util.BitSize
import com.karbonpowered.engine.util.collection.map.palette.AtomicPaletteIntStore

/**
 * Represents a cube containing 16x16x16 Blocks
 */
class KarbonChunk(
    val region: KarbonRegion,
    val x: Int,
    val y: Int,
    val z: Int,
    val blockStore: AtomicPaletteIntStore
) {
    fun cancelUnload(): Boolean {
        TODO()
    }

    companion object {
        /**
         * Stores the size of the amount of blocks in this Chunk
         */
        val BLOCKS = BitSize(4)
    }
}
