package com.karbonpowered.math.vector

open class IntVector2(
    open val data: IntArray
) : IntVector {
    open val x: Int get() = data[0]
    open val y: Int get() = data[1]

    override fun toIntArray(): IntArray = intArrayOf(x, y)
}