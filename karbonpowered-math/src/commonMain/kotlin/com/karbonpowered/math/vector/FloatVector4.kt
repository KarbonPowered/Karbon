package com.karbonpowered.math.vector

open class FloatVector4(
    override val x: Float = 0f,
    override val y: Float = 0f,
    override val z: Float = 0f,
    open val w: Float = 0f
) : FloatVector3(x, y) {
    override fun toString(): String = "($x, $y, $z, $w)"

    override fun toArray(): FloatArray = floatArrayOf(x, y, z, w)
}

