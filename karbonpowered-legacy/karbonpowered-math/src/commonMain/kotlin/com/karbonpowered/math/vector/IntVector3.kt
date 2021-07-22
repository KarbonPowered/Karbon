@file:Suppress("NOTHING_TO_INLINE")

package com.karbonpowered.math.vector

interface IntVector3 {
    val array: IntArray
    val x: Int get() = array[0]
    val y: Int get() = array[1]
    val z: Int get() = array[2]

    operator fun component1() = x
    operator fun component2() = y
    operator fun component3() = z
}

fun IntVector3(array: IntArray): IntVector3 = IntVector3Impl(array)
fun IntVector3(vector: IntVector3): IntVector3 = IntVector3Impl(vector.array)
fun IntVector3(x: Int, y: Int, z: Int): IntVector3 = IntVector3Impl(intArrayOf(x, y, z))

internal class IntVector3Impl(
    override var array: IntArray
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

interface MutableIntVector3 : IntVector3 {
    override var array: IntArray
    override var x: Int
    override var y: Int
    override var z: Int
}

fun MutableIntVector3(array: IntArray): MutableIntVector3 = MutableIntVector3Impl(array)
fun MutableIntVector3(vector: IntVector3): MutableIntVector3 = MutableIntVector3Impl(vector.array)
fun MutableIntVector3(x: Int, y: Int, z: Int): MutableIntVector3 = MutableIntVector3Impl(intArrayOf(x, y, z))

internal class MutableIntVector3Impl(
    override var array: IntArray
) : MutableIntVector3 {
    override var x: Int
        get() = array[0]
        set(value) {
            array[0] = value
        }
    override var y: Int
        get() = array[1]
        set(value) {
            array[1] = value
        }
    override var z: Int
        get() = array[2]
        set(value) {
            array[2] = value
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MutableIntVector3Impl

        if (!array.contentEquals(other.array)) return false

        return true
    }

    override fun hashCode(): Int = array.contentHashCode()

    override fun toString(): String = "($x, $y, $z)"
}

//class ImplIntVector3(
//    val data: IntArray = intArrayOf(0, 0, 0)
//) : Comparable<IntVector3> {
//    val x get() = data[0]
//    val y get() = data[1]
//    val z get() = data[2]
//    val lengthSquared: Int get() = x * x + y * y + z * z
//    val length: Float get() = sqrt(lengthSquared.toDouble()).toFloat()
//    val minAxis: Int get() = if (x < y) if (x < z) 0 else 2 else if (y < z) 1 else 2
//    val maxAxis: Int get() = if (x < y) if (y < z) 2 else 1 else if (x < z) 2 else 0
//
//    constructor(x: Int, y: Int, z: Int) : this(intArrayOf(x, y, z))
//    constructor(x: Double, y: Double, z: Double) : this(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    constructor(x: Float, y: Float, z: Float) : this(x.roundToInt(), y.roundToInt(), z.roundToInt())
//
//    fun add(vector: IntVector3): IntVector3 = add(vector.x, vector.y, vector.z)
//    fun add(x: Double, y: Double, z: Double): IntVector3 = add(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun add(x: Float, y: Float, z: Float): IntVector3 = add(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun add(x: Int, y: Int, z: Int): IntVector3 =
//        IntVector3(this.x + x, this.y + y, this.z + z)
//
//    fun sub(vector: IntVector3): IntVector3 = sub(vector.x, vector.y, vector.z)
//    fun sub(x: Double, y: Double, z: Double): IntVector3 = sub(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun sub(x: Float, y: Float, z: Float): IntVector3 = sub(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun sub(x: Int, y: Int, z: Int): IntVector3 =
//        IntVector3(this.x - x, this.y - y, this.z - z)
//
//    fun mul(a: Double): IntVector3 = mul(a.roundToInt(), a.roundToInt(), a.roundToInt())
//    fun mul(a: Float): IntVector3 = mul(a.roundToInt(), a.roundToInt(), a.roundToInt())
//    fun mul(a: Int): IntVector3 = mul(a, a, a)
//    fun mul(vector: IntVector3): IntVector3 = mul(vector.x, vector.y, vector.z)
//    fun mul(x: Int, y: Int, z: Int): IntVector3 =
//        IntVector3(this.x * x, this.y * y, this.z * z)
//
//    fun div(a: Double): IntVector3 = div(a.roundToInt(), a.roundToInt(), a.roundToInt())
//    fun div(a: Float): IntVector3 = div(a.roundToInt(), a.roundToInt(), a.roundToInt())
//    fun div(a: Int): IntVector3 = div(a, a, a)
//    fun div(vector: IntVector3): IntVector3 = div(vector.x, vector.y, vector.z)
//    fun div(x: Int, y: Int, z: Int): IntVector3 =
//        IntVector3(this.x / x, this.y / y, this.z / z)
//
//    fun dot(vector: IntVector3): Int = dot(vector.x, vector.y, vector.z)
//    fun dot(x: Double, y: Double, z: Double): Int = dot(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun dot(x: Float, y: Float, z: Float): Int = dot(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun dot(x: Int, y: Int, z: Int): Int = this.x * x + this.y * y + this.z * z
//
//    fun project(vector: IntVector3): IntVector3 = project(vector.x, vector.y, vector.z)
//    fun project(x: Double, y: Double, z: Double): IntVector3 = project(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun project(x: Float, y: Float, z: Float): IntVector3 = project(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun project(x: Int, y: Int, z: Int): IntVector3 {
//        val lengthSquared = lengthSquared
//        if (lengthSquared == 0) {
//            throw ArithmeticException("Cannot project onto the zero vector")
//        } else {
//            val a = dot(x, y, z).toFloat() / lengthSquared.toFloat()
//            return IntVector3(a * x, a * y, a * z)
//        }
//    }
//
//    fun cross(vector: IntVector3): IntVector3 = cross(vector.x, vector.y, vector.z)
//    fun cross(x: Double, y: Double, z: Double): IntVector3 = cross(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun cross(x: Float, y: Float, z: Float): IntVector3 = cross(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun cross(x: Int, y: Int, z: Int): IntVector3 =
//        IntVector3(
//            this.y * z - this.z * y,
//            this.z * x - this.x * z,
//            this.x * y - this.y * x
//        )
//
//    fun pow(power: Double): IntVector3 =
//        IntVector3(
//            y.toDouble().pow(power),
//            y.toDouble().pow(power),
//            z.toDouble().pow(power)
//        )
//
//    fun pow(power: Float): IntVector3 =
//        IntVector3(y.toFloat().pow(power), y.toFloat().pow(power), z.toFloat().pow(power))
//
//    fun pow(power: Int): IntVector3 =
//        IntVector3(
//            y.toDouble().pow(power),
//            y.toDouble().pow(power),
//            z.toDouble().pow(power)
//        )
//
//    fun abs(): IntVector3 = IntVector3(x.absoluteValue, y.absoluteValue, z.absoluteValue)
//    fun negate(): IntVector3 = IntVector3(-x, -y, -z)
//
//    fun min(vector: IntVector3): IntVector3 = min(vector.x, vector.y, vector.z)
//    fun min(x: Double, y: Double, z: Double): IntVector3 = min(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun min(x: Float, y: Float, z: Float): IntVector3 = min(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun min(x: Int, y: Int, z: Int): IntVector3 =
//        IntVector3(min(this.x, x), min(this.y, y), min(this.z, z))
//
//    fun max(vector: IntVector3): IntVector3 = max(vector.x, vector.y, vector.z)
//    fun max(x: Double, y: Double, z: Double): IntVector3 = max(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun max(x: Float, y: Float, z: Float): IntVector3 = max(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun max(x: Int, y: Int, z: Int): IntVector3 = IntVector3(max(this.x, x), max(this.y, y), max(this.z, z))
//
//    fun distanceSquared(vector: IntVector3): Int = distanceSquared(vector.x, vector.y, vector.z)
//    fun distanceSquared(x: Double, y: Double, z: Double): Int =
//        distanceSquared(x.roundToInt(), y.roundToInt(), z.roundToInt())
//
//    fun distanceSquared(x: Float, y: Float, z: Float): Int =
//        distanceSquared(x.roundToInt(), y.roundToInt(), z.roundToInt())
//
//    fun distanceSquared(x: Int, y: Int, z: Int): Int {
//        val dx = this.x - x
//        val dy = this.y - y
//        val dz = this.z - z
//        return dx * dx + dy * dy + dz * dz
//    }
//
//    fun distance(vector: IntVector3): Float = distance(vector.x, vector.y, vector.z)
//    fun distance(x: Double, y: Double, z: Double): Float = distance(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun distance(x: Float, y: Float, z: Float): Float = distance(x.roundToInt(), y.roundToInt(), z.roundToInt())
//    fun distance(x: Int, y: Int, z: Int): Float = sqrt(distanceSquared(x, y, z).toDouble()).toFloat()
//
//    fun toInt(): IntVector3 = IntVector3(x, y, z)
//    fun toDouble(): DoubleVector3 = DoubleVector3(x.toDouble(), y.toDouble(), z.toDouble())
//
//    fun copy(): IntVector3 = IntVector3(data)
//
//    override fun compareTo(other: IntVector3): Int = lengthSquared - other.lengthSquared
//
//    operator fun component1() = x
//    operator fun component2() = y
//    operator fun component3() = z
//
//    override fun toString(): String = "($x, $y, $z)"
//
//    private val hashCode= data.contentHashCode()
//    override fun hashCode(): Int = hashCode
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other == null || this::class != other::class) return false
//
//        other as IntVector3
//
//        if (!data.contentEquals(other.data)) return false
//
//        return true
//    }
//
//    companion object {
//        val ZERO = IntVector3(0, 0, 0)
//        val UNIT_X = IntVector3(1, 0, 0)
//        val RIGHT = UNIT_X
//        val UNIT_Y = IntVector3(0, 1, 0)
//        val UP = UNIT_Y
//        val UNIT_Z = IntVector3(0, 0, 1)
//        val FORWARD = UNIT_Z
//        val ONE = IntVector3(1, 1, 1)
//
//        fun of(n: Int): IntVector3 = when (n) {
//            0 -> ZERO
//            1 -> ONE
//            else -> IntVector3(n, n, n)
//        }
//
//        fun of(x: Int, y: Int, z: Int): IntVector3 = when {
//            x == 0 && y == 0 && z == 0 -> ZERO
//            x == 1 && y == 1 && z == 1 -> ONE
//            x == 1 && y == 0 && z == 0 -> UNIT_X
//            x == 0 && y == 1 && z == 0 -> UNIT_Y
//            x == 0 && y == 0 && z == 1 -> UNIT_Z
//            else -> IntVector3(x, y, z)
//        }
//    }
//}

//inline fun intVector3Of(n: Int): IntVector3 = IntVector3.of(n)
//inline fun intVector3Of(x: Int, y: Int, z: Int): IntVector3 = IntVector3.of(x, y, z)
//
//inline operator fun IntVector3.component1(): Int = x
//inline operator fun IntVector3.component2(): Int = y
//inline operator fun IntVector3.component3(): Int = z
//
//inline operator fun IntVector3.plus(vector: IntVector3): IntVector3 = add(vector)
//inline operator fun IntVector3.minus(vector: IntVector3): IntVector3 = sub(vector)
//
//inline operator fun IntVector3.times(vector: IntVector3): IntVector3 = mul(vector)
//inline operator fun IntVector3.times(a: Int): IntVector3 = mul(a)
//inline operator fun IntVector3.times(a: Double): IntVector3 = mul(a)
//inline operator fun IntVector3.times(a: Float): IntVector3 = mul(a)
//
//inline operator fun IntVector3.div(vector: IntVector3): IntVector3 = div(vector)
//inline operator fun IntVector3.div(a: Int): IntVector3 = div(a)
//inline operator fun IntVector3.div(a: Double): IntVector3 = div(a)
//inline operator fun IntVector3.div(a: Float): IntVector3 = div(a)