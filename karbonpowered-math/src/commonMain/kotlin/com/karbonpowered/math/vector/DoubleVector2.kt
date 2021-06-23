package com.karbonpowered.math.vector

interface DoubleVector2 : DoubleVector, Vector2<Double> {
    override fun toDoubleArray(): DoubleArray = doubleArrayOf(x, y)
}