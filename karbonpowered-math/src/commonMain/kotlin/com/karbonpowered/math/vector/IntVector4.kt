package com.karbonpowered.math.vector

open class IntVector4(
    override val x: Int = 0,
    override val y: Int = 0,
    override val z: Int = 0,
    open val w: Int = 0
) : IntVector3(x,y,z) {
    override fun toArray(): IntArray = intArrayOf(x,y,z,w)

    override fun toString(): String = "($x, $y, $z, $w)"
}