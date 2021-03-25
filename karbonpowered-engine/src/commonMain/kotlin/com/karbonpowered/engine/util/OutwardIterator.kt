package com.karbonpowered.engine.util

import com.karbonpowered.math.vector.IntVector3
import com.karbonpowered.math.vector.MutableIntVector3

class OutwardIterator(
    x: Int = 0,
    y: Int = 0,
    z: Int = 0,
    maxDistance: Int = Int.MAX_VALUE
) : MutableIntVector3(x, y, z), Iterator<IntVector3> {
    val center = IntVector3(x, y, z)
    private val step = MutableIntVector3()
    private var first = true
    private val endDistance = maxDistance
    private var hasNext = true
    var distance = 0
        private set

    override fun hasNext(): Boolean = hasNext

    override fun next(): IntVector3 {
        if (!hasNext) {
            throw NoSuchElementException("The Outward Iterator ran out of elements")
        }
        // First block is always the central block
        if (first) {
            step.x = 0
            step.z = 0
            first = false
            if (endDistance <= 0) {
                hasNext = false
            }
        } else {
            val dx = x - center.x
            val dy = y - center.y
            val dz = z - center.z

            // Last block was top of layer, move to start of next layer
            if (dx == 0 && dz == 0 && dy >= 0) {
                y = (center.y shl 1) - y - 1
                step.x = 0
                step.z = 0
                distance++
            } else if (dx == 0) {
                // Reached end of horizontal slice
                // Move up to next slice
                if (dz >= 0) {
                    step.x = 1
                    step.z = -1
                    y++

                    // Bottom half of layer
                    if (dy < 0) {
                        z++
                        // Top half of layer
                    } else {
                        z--
                        // Reached top of layer
                        if (z == center.z) {
                            step.x = 0
                            step.z = 0
                        }
                    }
                    // Change direction (50% of horizontal slice complete)
                } else {
                    step.x = -1
                    step.z = 1
                }
            } else if (dz == 0) {
                // Change direction (25% of horizontal slice complete)
                if (dx > 0) {
                    step.x = -1
                    step.z = -1
                    // Change direction (75% of horizontal slice compete)
                } else {
                    step.x = 1
                    step.z = 1
                }
            }
            add(step)
            if (distance == 0 || (dx == 0 && dz == 1 && dy >= endDistance - 1)) {
                hasNext = false
            }
        }
        return this
    }
}