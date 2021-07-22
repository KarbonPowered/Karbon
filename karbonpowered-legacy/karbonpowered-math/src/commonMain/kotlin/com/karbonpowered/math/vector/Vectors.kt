package com.karbonpowered.math.vector

interface Vector<T : Number>

interface Vector1<T : Number> : Vector<T> {
    val x: T

    operator fun component0() = x
}

interface Vector2<T : Number> : Vector1<T> {
    val y: T

    operator fun component1() = y
}

interface Vector3<T : Number> : Vector2<T> {
    val z: T

    operator fun component2() = z
}

interface Vector4<T : Number> : Vector3<T> {
    val w: T

    operator fun component3() = w
}

interface IntVector : Vector<Int> {
    fun toIntArray(): IntArray
}

interface FloatVector : Vector<Float> {
    fun toFloatArray(): FloatArray
}

interface DoubleVector : Vector<Double> {
    fun toDoubleArray(): DoubleArray
}