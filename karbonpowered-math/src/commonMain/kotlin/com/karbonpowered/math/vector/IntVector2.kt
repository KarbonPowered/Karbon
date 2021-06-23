package com.karbonpowered.math.vector

open class IntVector2(
    open val x: Int,
    open val y: Int
) : IntVector {
    override fun toIntArray(): IntArray = intArrayOf(x, y)
}