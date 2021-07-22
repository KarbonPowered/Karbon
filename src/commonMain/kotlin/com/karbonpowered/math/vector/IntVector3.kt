package com.karbonpowered.math.vector

interface IntVector3 {
    val array: IntArray
    val x: Int get() = array[0]
    val y: Int get() = array[1]
    val z: Int get() = array[2]

    operator fun component1() = x
    operator fun component2() = y
    operator fun component3() = z

    override fun toString(): String
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}

interface MutableIntVector3 : IntVector3 {
    override var x: Int
    override var y: Int
    override var z: Int
}

private class IntVector3Impl(
    override val array: IntArray
) : IntVector3 {
    private val hashCode = array.contentHashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as IntVector3Impl

        if (!array.contentEquals(other.array)) return false

        return true
    }

    override fun hashCode(): Int = hashCode

    override fun toString(): String = "($x, $y, $z)"
}

fun IntVector3(x: Int, y: Int, z: Int): IntVector3 = IntVector3Impl(intArrayOf(x, y, z))
fun IntVector3(array: IntArray): IntVector3 = IntVector3Impl(array)