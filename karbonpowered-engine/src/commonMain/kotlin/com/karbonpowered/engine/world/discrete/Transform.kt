package com.karbonpowered.engine.world.discrete

import com.karbonpowered.math.imaginary.FloatQuaternion
import com.karbonpowered.math.vector.FloatVector3

data class Transform(
    val position: Position = Position.INVALID,
    val rotation: FloatQuaternion = FloatQuaternion.IDENTITY,
    val scale: FloatVector3 = FloatVector3.ONE
) {
    constructor(transform: Transform) : this(transform.position, transform.rotation, transform.scale)

    val isValid get() = this != INVALID

    companion object {
        val INVALID = Transform()
    }
}