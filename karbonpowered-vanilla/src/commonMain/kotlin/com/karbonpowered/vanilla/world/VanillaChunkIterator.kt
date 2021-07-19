package com.karbonpowered.vanilla.world

import com.karbonpowered.engine.util.ChunkIterator
import com.karbonpowered.math.vector.IntVector3

class VanillaChunkIterator(
    val distance: Int,
    val minY: Int,
    val maxY: Int
) : ChunkIterator {
    override fun iterator(centerX: Int, centerY: Int, centerZ: Int): Iterator<IntVector3> =
        IteratorImpl(centerX, minY, centerZ)

    private inner class IteratorImpl(
        var x: Int,
        var y: Int,
        var z: Int
    ) : Iterator<IntVector3> {
        val maxChunks = (distance * 2 + 1) * (distance * 2 + 1)
        var dx = 1
        var dz = 0
        var segmentPassed = 0
        var segmentLength = 1
        var steps = 1

        override fun hasNext(): Boolean = steps < maxChunks

        override fun next(): IntVector3 {
            val vector = IntVector3(x, y, z)
            y += 1

            if (y > maxY) {
                segmentPassed++
                y = minY
                x += dx
                z += dz
                steps++
            }

            if (segmentPassed == segmentLength) {
                segmentPassed = 0
                val b = dx
                dx = -dz
                dz = b
                if (dx == 0) {
                    segmentLength++
                }
            }

            return vector
        }
    }
}