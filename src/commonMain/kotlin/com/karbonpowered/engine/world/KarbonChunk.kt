package com.karbonpowered.engine.world

import com.karbonpowered.engine.util.BitSize
import com.karbonpowered.engine.util.collection.map.palette.AtomicPaletteIntStore

class KarbonChunk(
    val region: KarbonRegion,
    val chunkX: Int,
    val chunkY: Int,
    val chunkZ: Int,
    val blockStore: AtomicPaletteIntStore
) {
    override fun toString() = "Chunk(${region.world}, $chunkX, $chunkY, $chunkZ)"

    companion object {
        /**
         * Stores the size of the amount of blocks in this Chunk
         */
        val BLOCKS = BitSize(4)

        fun basePosition(position: Position) = basePosition(
            position.world,
            position.chunkX,
            position.chunkY,
            position.chunkZ
        )

        fun basePosition(world: KarbonWorld, x: Int, y: Int, z: Int) = Position(
            world,
            (x shl BLOCKS.BITS).toFloat(),
            (y shl BLOCKS.BITS).toFloat(),
            (z shl BLOCKS.BITS).toFloat()
        )
    }
}