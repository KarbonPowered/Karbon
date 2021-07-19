package com.karbonpowered.vanilla.world

import com.karbonpowered.engine.util.ChunkIterator
import com.karbonpowered.math.vector.IntVector3

class VanillaChunkIterator(
    val distance: Int,
    val minY: Int,
    val maxY: Int
) : ChunkIterator {
    override fun iterator(centerX: Int, centerY: Int, centerZ: Int): Iterator<IntVector3> = iterator {
        for (x in centerX - distance..centerX + distance) {
            for (z in centerZ - distance..centerZ + distance) {
                for (y in minY..maxY) {
                    yield(IntVector3(x, y, z))
                }
            }
        }
    }
}