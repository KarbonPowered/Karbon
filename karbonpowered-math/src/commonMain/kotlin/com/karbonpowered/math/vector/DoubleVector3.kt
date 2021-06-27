package com.karbonpowered.math.vector

import kotlin.math.floor

interface DoubleVector3 : DoubleVector2, Vector3<Double> {
    val floorX get() = floor(x)
    val floorY get() = floor(y)
    val floorZ get() = floor(z)

    fun add(x: Double, y: Double, z: Double): DoubleVector3
    fun sub(x: Double, y: Double, z: Double): DoubleVector3
    fun mul(x: Double, y: Double, z: Double): DoubleVector3
    fun div(x: Double, y: Double, z: Double): DoubleVector3

    operator fun plus(vector: DoubleVector3): DoubleVector3 = add(vector.x, vector.y, vector.z)
    operator fun minus(vector: DoubleVector3): DoubleVector3 = sub(vector.x, vector.y, vector.z)
    operator fun times(vector: DoubleVector3): DoubleVector3 = mul(vector.x, vector.y, vector.z)
    operator fun div(vector: DoubleVector3): DoubleVector3 = div(vector.x, vector.y, vector.z)

    fun distanceSquared(x: Double, y: Double, z: Double): Double {
        val dx = this.x - x
        val dy = this.y - y
        val dz = this.z - z
        return dx * dx + dy * dy + dz * dz
    }

    override fun toDoubleArray(): DoubleArray = doubleArrayOf(x, y, z)
}

interface MutableDoubleVector3 : DoubleVector3 {
    override var x: Double
    override var y: Double
    override var z: Double
}

open class BaseDoubleVector3(
    override val x: Double,
    override val y: Double,
    override val z: Double
) : DoubleVector3 {
    private val hashCode by lazy {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result
    }

    override fun add(x: Double, y: Double, z: Double): DoubleVector3 =
        mutableDoubleVector3of(this).add(x, y, z)

    override fun sub(x: Double, y: Double, z: Double): DoubleVector3 =
        mutableDoubleVector3of(this).sub(x, y, z)

    override fun mul(x: Double, y: Double, z: Double): DoubleVector3 =
        mutableDoubleVector3of(this).mul(x, y, z)

    override fun div(x: Double, y: Double, z: Double): DoubleVector3 =
        mutableDoubleVector3of(this).div(x, y, z)

    override fun toString(): String = "($x, $y, $z)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DoubleVector3) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        return true
    }

    override fun hashCode(): Int = hashCode
}

open class BaseMutableDoubleVector3(
    override var x: Double = 0.0,
    override var y: Double = 0.0,
    override var z: Double = 0.0
) : BaseDoubleVector3(x, y, z), MutableDoubleVector3 {
    override fun add(x: Double, y: Double, z: Double): DoubleVector3 = apply {
        this.x += x
        this.y += y
        this.z += z
    }

    override fun sub(x: Double, y: Double, z: Double): DoubleVector3 = apply {
        this.x -= x
        this.y -= y
        this.z -= z
    }

    override fun mul(x: Double, y: Double, z: Double): DoubleVector3 = apply {
        this.x *= x
        this.y *= y
        this.z *= z
    }

    override fun div(x: Double, y: Double, z: Double): DoubleVector3 = apply {
        this.x /= x
        this.y /= x
        this.z /= x
    }

    override fun equals(other: Any?): Boolean = super.equals(other)

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }
}

fun DoubleVector3(
    vector: DoubleVector3
): DoubleVector3 = BaseDoubleVector3(vector.x, vector.y, vector.z)

fun DoubleVector3(
    x: Double = 0.0,
    y: Double = 0.0,
    z: Double = 0.0
): DoubleVector3 = BaseDoubleVector3(x, y, z)

fun mutableDoubleVector3of(
    vector: DoubleVector3
): MutableDoubleVector3 = BaseMutableDoubleVector3(vector.x, vector.y, vector.z)

fun mutableDoubleVector3of(
    x: Double = 0.0,
    y: Double = 0.0,
    z: Double = 0.0
): MutableDoubleVector3 = BaseMutableDoubleVector3(x, y, z)