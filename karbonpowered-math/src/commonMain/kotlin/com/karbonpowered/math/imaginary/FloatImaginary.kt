package com.karbonpowered.math.imaginary

/**
 * Represents an imaginary number.
 */
interface FloatImaginary {
    val length: Float
    val lengthSquared: Float

    fun normalize(): FloatImaginary
}
