package com.karbonpowered.engine.util.cuboid

import com.karbonpowered.engine.world.Position
import com.karbonpowered.math.vector.FloatVector3

/**
 * Represents a Cube that is located somewhere in a world.
 */
open class Cube(base: Position, size: Float) : Cuboid(base, FloatVector3(size, size, size))