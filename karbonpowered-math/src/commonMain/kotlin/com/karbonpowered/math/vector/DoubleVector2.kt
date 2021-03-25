package com.karbonpowered.math.vector

interface DoubleVector2 : DoubleVector {
    val x: Double
    val y: Double

    override fun toDoubleArray(): DoubleArray = doubleArrayOf(x, y)
}