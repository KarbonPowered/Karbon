package com.karbonpowered.engine.util

import com.karbonpowered.math.vector.IntVector3

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
    private var current = IntVector3(x, y, z)
    private val center = IntVector3(x, y, z)
    private var step = IntVector3(x, y, z)
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
            step = IntVector3(0, step.y, 0)
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
                current = IntVector3(current.x, (center.y shl 1) - current.y - 1, current.z)
                step = IntVector3(0, step.y, 0)
                distance++
            } else if (dx == 0) {
                // Reached end of horizontal slice
                // Move up to next slice
                if (dz >= 0) {
                    step = IntVector3(1, step.y, -1)
                    current = current.add(0, 1, 0)

                    // Bottom half of layer
                    if (dy < 0) {
                        current = current.add(0, 0, 1)
                        // Top half of layer
                    } else {
                        current = current.add(0, 0, -1)
                        // Reached top of layer
                        if (current.z == center.z) {
                            step = IntVector3(0, step.y, 0)
                        }
                    }
                    // Change direction (50% of horizontal slice complete)
                } else {
                    step = IntVector3(-1, step.y, 1)
                }
            } else if (dz == 0) {
                // Change direction (25% of horizontal slice complete)
                step = if (dx > 0) {
                    IntVector3(-1, step.y, -1)
                    // Change direction (75% of horizontal slice compete)
                } else {
                    IntVector3(1, step.y, 1)
                }
            }
            current = current.add(step)
            if (distance == 0 || (dx == 0 && dz == 1 && dy >= endDistance - 1)) {
                hasNext = false
            }
        }
        return current
    }
}