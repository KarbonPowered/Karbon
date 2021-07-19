package com.karbonpowered.vanilla.world

import com.karbonpowered.engine.util.ChunkIterator
import com.karbonpowered.math.vector.IntVector3
import kotlin.math.abs

class VanillaChunkIterator(
    val maxDistance: Int,
    val minY: Int,
    val maxY: Int
) : ChunkIterator {
    override fun iterator(centerX: Int, centerY: Int, centerZ: Int): Iterator<IntVector3> {
        val side = maxDistance * 2 + 1
        val list = ArrayList<IntVector3>(side * side * (abs(minY) + maxY))
        for (y in minY..maxY) {
            for (x in centerX - maxDistance..centerX + maxDistance) {
                for (z in centerZ - maxDistance..centerZ + maxDistance) {
                    list.add(IntVector3(x, y, z))
                }
            }
        }
        return list.iterator()
    }
}