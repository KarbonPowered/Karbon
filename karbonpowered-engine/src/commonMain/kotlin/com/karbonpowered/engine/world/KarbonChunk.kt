package com.karbonpowered.engine.world

import com.karbonpowered.engine.util.BitSize
import com.karbonpowered.engine.util.collection.map.palette.AtomicPaletteIntStore
import com.karbonpowered.engine.world.cuboid.Cube
import com.karbonpowered.engine.world.discrete.Position

/**
 * Represents a cube containing 16x16x16 Blocks
 */
class KarbonChunk(
    val region: KarbonRegion,
    x: Int,
    y: Int,
    z: Int,
    val blockStore: AtomicPaletteIntStore
) : Cube(
    Position(
        region.world,
        (x shl BLOCKS.BITS).toFloat(),
        (y shl BLOCKS.BITS).toFloat(),
        (z shl BLOCKS.BITS).toFloat()
    ),
    BLOCKS.SIZE.toFloat()
) {
    val isLoaded: Boolean = true

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
