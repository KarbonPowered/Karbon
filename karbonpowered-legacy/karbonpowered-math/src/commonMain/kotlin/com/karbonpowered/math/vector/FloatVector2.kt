package com.karbonpowered.math.vector

open class FloatVector2(
    open val x: Float,
    open val y: Float
) : FloatVector {
    override fun toFloatArray(): FloatArray = floatArrayOf(x, y)

    override fun toString(): String = "($x, $y)"
}