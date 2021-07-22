package com.karbonpowered.math.vector

import kotlin.math.floor

interface FloatVector3 {
    val array: FloatArray
    val x: Float get() = array[0]
    val y: Float get() = array[1]
    val z: Float get() = array[2]

    val floorX get() = floor(x)
    val floorY get() = floor(y)
    val floorZ get() = floor(z)

    operator fun component1() = x
    operator fun component2() = y
    operator fun component3() = z

    operator fun plus(other: FloatVector3): FloatVector3

    override fun toString(): String
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}

interface MutableFloatVector3 : FloatVector3 {
    override var x: Float
    override var y: Float
    override var z: Float
}

private class FloatVector3Impl(
    override val array: FloatArray
) : FloatVector3 {
    private val hashCode = array.contentHashCode()

    override fun plus(other: FloatVector3): FloatVector3 =
        FloatVector3(x + other.x, y + other.y, z + other.z)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FloatVector3Impl

        if (!array.contentEquals(other.array)) return false

        return true
    }

    override fun hashCode(): Int = hashCode

    override fun toString(): String = "($x, $y, $z)"
}

fun FloatVector3(x: Float, y: Float, z: Float): FloatVector3 = FloatVector3Impl(floatArrayOf(x, y, z))
fun FloatVector3(array: FloatArray): FloatVector3 = FloatVector3Impl(array)