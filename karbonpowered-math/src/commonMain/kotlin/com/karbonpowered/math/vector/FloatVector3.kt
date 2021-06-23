package com.karbonpowered.math.vector

interface FloatVector3 : Vector3<Float> {
    operator fun plus(vector: FloatVector3): FloatVector3 = add(vector)
    fun add(x: Float, y: Float, z: Float): FloatVector3
    fun add(vector: FloatVector3): FloatVector3 = add(vector.x, vector.y, vector.z)

    companion object {
        val ZERO = BaseFloatVector3(0f, 0f, 0f)
        val ONE = BaseFloatVector3(1f, 1f, 1f)
        val UNIT_X = BaseFloatVector3(1f, 0f, 0f)
        val UNIT_Y = BaseFloatVector3(0f, 1f, 0f)
        val UNIT_Z = BaseFloatVector3(0f, 0f, 1f)
        val RIGHT = UNIT_X
        val UP = UNIT_Y
        val FORWARD = UNIT_Z
    }
}

open class BaseFloatVector3(
    override val x: Float,
    override val y: Float,
    override val z: Float
) : FloatVector3 {
    override fun add(x: Float, y: Float, z: Float): FloatVector3 = FloatVector3(this.x + x, this.y + y, this.z + z)
}

fun FloatVector3() = FloatVector3.ZERO

fun FloatVector3(value: Float): FloatVector3 = when (value) {
    0f -> FloatVector3.ZERO
    1f -> FloatVector3.ONE
    else -> BaseFloatVector3(value, value, value)
}

fun FloatVector3(x: Float, y: Float, z: Float): FloatVector3 = when {
    x == 0f && y == 0f && z == 0f -> FloatVector3.ZERO
    x == 1f && y == 1f && z == 1f -> FloatVector3.ONE
    else -> BaseFloatVector3(x, y, z)
}
