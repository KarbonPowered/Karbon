package com.karbonpowered.engine.util

import com.karbonpowered.math.vector.IntVector3
import com.karbonpowered.math.vector.MutableIntVector3

/**
 * An Iterator that iterates outwards from a given central 3d integer coordinate
 *
 * The [Manhattan distance](https://en.wikipedia.org/wiki/Taxicab_geometry) from the given center to the coordinates in the sequence
 * increases monotonically and the iterator passes through all integer coordinates.
 */
class OutwardIterator(
    x: Int = 0,
    y: Int = 0,
    z: Int = 0,
    maxDistance: Int = Int.MAX_VALUE
) : Iterator<IntVector3> {
    private var current = MutableIntVector3(x, y, z)
    private val center = IntVector3(x, y, z)
    private var step = MutableIntVector3(0, 0, 0)
    private var first = true
    private var endDistance = maxDistance
    private var hasNext = endDistance >= 0
    var distance = 0
        private set

    override fun hasNext(): Boolean = hasNext

    override fun next(): IntVector3 {
        check(hasNext) { throw NoSuchElementException() }

        // First block is always the central block
        if (first) {
            first = false
            if (endDistance <= 0) {
                hasNext = false
            }
        } else {
            val dx = current.x - center.x
            val dy = current.y - center.y
            val dz = current.z - center.z

            // Last block was top of layer, move to start of next layer
            if (dx == 0 && dz == 0 && dy >= 0) {
                current.y = (center.y shl 1) - current.y - 1
                step.x = 0
                step.z = 0
                distance++
            } else if (dx == 0) {
                // Reached end of horizontal slice
                // Move up to next slice
                if (dz >= 0) {
                    step.x = 1
                    step.z = -1
                    current.y++

                    // Bottom half of layer
                    if (dy < 0) {
                        current.z++
                        // Top half of layer
                    } else {
                        current.z--
                        // Reached top of layer
                        if (current.z == center.z) {
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
            current.x += step.x
            current.y += step.y
            current.z += step.z
            if (distance == 0 || (dx == 0 && dz == 1 && dy >= endDistance - 1)) {
                hasNext = false
            }
        }
        return current
    }
}