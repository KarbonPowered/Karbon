package com.karbonpowered.engine.world

import com.karbonpowered.engine.util.BitSize
import com.karbonpowered.engine.util.collection.map.palette.AtomicPaletteIntStore
import com.karbonpowered.engine.world.cuboid.Cube
import com.karbonpowered.engine.world.discrete.Position
import com.karbonpowered.engine.world.reference.WorldReference

/**
 * Represents a cube containing 16x16x16 Blocks
 */
class KarbonChunk(
    val region: KarbonRegion,
    val chunkX: Int,
    val chunkY: Int,
    val chunkZ: Int,
    val blockStore: AtomicPaletteIntStore
) : Cube(
    basePosition(region.world, chunkX, chunkY, chunkZ),
    BLOCKS.SIZE.toFloat()
) {
    val isLoaded: Boolean = true

    override fun toString() = "Chunk($world, $chunkX, $chunkY, $chunkZ)"

    companion object {
        /**
         * Stores the size of the amount of blocks in this Chunk
         */
        val BLOCKS = BitSize(4)

        fun basePosition(world: WorldReference, x: Int, y: Int, z: Int) = Position(
            world,
            (x shl BLOCKS.BITS).toFloat(),
            (y shl BLOCKS.BITS).toFloat(),
            (z shl BLOCKS.BITS).toFloat()
        )
    }
}
