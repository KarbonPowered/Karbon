package com.karbonpowered.math.imaginary

import com.karbonpowered.math.FLT_EPSILON
import com.karbonpowered.math.toRadians
import com.karbonpowered.math.vector.FloatVector3
import com.karbonpowered.math.vector.Vector4
import kotlinx.serialization.Serializable
import kotlin.math.*

/**
 * Represent a quaternion of the form `xi + yj + zk + w`.
 *
 * The x, y, z and w components are stored as floats. This class is immutable.
 */
interface FloatQuaternion : FloatImaginary, Comparable<FloatQuaternion>, Vector4<Float> {
    override val x: Float
    override val y: Float
    override val z: Float
    override val w: Float

    override val lengthSquared: Float
    override val length: Float

    /**
     * Returns a unit vector representing the direction of this quaternion,
     * which is [FloatVector3.FORWARD] rotated by this quaternion.
     *
     * @return The vector representing the direction this quaternion is pointing to
     */
    val direction: FloatVector3 get() = rotate(FloatVector3.FORWARD)

    /**
     * Returns the axis of rotation for this quaternion.
     *
     * @return The axis of rotation
     */
    val axis: FloatVector3

    operator fun plus(quaternion: FloatQuaternion): FloatQuaternion
    fun add(x: Float, y: Float, z: Float, w: Float): FloatQuaternion

    operator fun minus(quaternion: FloatQuaternion): FloatQuaternion
    fun sub(x: Float, y: Float, z: Float, w: Float): FloatQuaternion

    operator fun times(quaternion: FloatQuaternion): FloatQuaternion
    fun mul(x: Float, y: Float, z: Float, w: Float): FloatQuaternion

    operator fun div(quaternion: FloatQuaternion): FloatQuaternion
    fun div(x: Float, y: Float, z: Float, w: Float): FloatQuaternion

    /**
     * Returns the dot product of this quaternion with another one.
     *
     * @param quaternion The quaternion to calculate the dot product with
     * @return The dot product of the two quaternions
     */
    fun dot(quaternion: FloatQuaternion): Float

    /**
     * Returns the dot product of this quaternion with the float components of another one.
     *
     * @param x The x (imaginary) component of the quaternion to calculate the dot product with
     * @param y The y (imaginary) component of the quaternion to calculate the dot product with
     * @param z The z (imaginary) component of the quaternion to calculate the dot product with
     * @param w The w (real) component of the quaternion to calculate the dot product with
     * @return The dot product of the two quaternions
     */
    fun dot(x: Float, y: Float, z: Float, w: Float): Float

    /**
     * Rotates a vector by this quaternion.
     *
     * @param vector The vector to rotate
     * @return The rotated vector
     */
    fun rotate(vector: FloatVector3): FloatVector3

    /**
     * Rotates the float components of a vector by this quaternion.
     *
     * @param x The x component of the vector
     * @param y The y component of the vector
     * @param z The z component of the vector
     * @return The rotated vector
     */
    fun rotate(x: Float, y: Float, z: Float): FloatVector3

    fun clone() = FloatQuaternion(this)

    companion object {
        val ZERO = BaseFloatQuaternion(0f, 0f, 0f, 0f)
        val IDENTITY = BaseFloatQuaternion(0f, 0f, 0f, 1f)

        /**
         * Creates a new quaternion from the float angles in radians around the x, y and z axes.
         *
         * @param pitch The rotation around x
         * @param yaw The rotation around y
         * @param roll The rotation around z
         * @return The quaternion defined by the rotations around the axes
         */
        fun fromAxesAnglesRad(pitch: Float, yaw: Float, roll: Float): FloatQuaternion =
            fromAngleRadAxis(pitch, FloatVector3.UNIT_X) *
                    fromAngleRadAxis(yaw, FloatVector3.UNIT_Y) *
                    fromAngleRadAxis(roll, FloatVector3.UNIT_Z)

        /**
         * Creates a new quaternion from the float angles in degrees around the x, y and z axes.
         *
         * @param pitch The rotation around x
         * @param yaw The rotation around y
         * @param roll The rotation around z
         * @return The quaternion defined by the rotations around the axes
         */
        fun fromAxesAnglesDeg(pitch: Float, yaw: Float, roll: Float): FloatQuaternion =
            fromAngleDegAxis(pitch, FloatVector3.UNIT_X) *
                    fromAngleDegAxis(yaw, FloatVector3.UNIT_Y) *
                    fromAngleDegAxis(roll, FloatVector3.UNIT_Z)

        fun fromAngleDegAxis(angle: Float, vector: FloatVector3): FloatQuaternion =
            fromAngleDegAxis(angle, vector.x, vector.y, vector.z)

        /**
         * Creates a new quaternion from the rotation float angle in degrees around the axis vector float components.
         *
         * @param angle The rotation angle in degrees
         * @param x The x component of the axis vector
         * @param y The y component of the axis vector
         * @param z The z component of the axis vector
         * @return The quaternion defined by the rotation around the axis
         */
        fun fromAngleDegAxis(angle: Float, x: Float, y: Float, z: Float): FloatQuaternion =
            fromAngleRadAxis(angle.toDouble().toRadians().toFloat(), x, y, z)

        fun fromAngleRadAxis(angle: Float, vector: FloatVector3): FloatQuaternion =
            fromAngleRadAxis(angle, vector.x, vector.y, vector.z)

        /**
         * Creates a new quaternion from the rotation float angle in radians around the axis vector float components.
         *
         * @param angle The rotation angle in radians
         * @param x The x component of the axis vector
         * @param y The y component of the axis vector
         * @param z The z component of the axis vector
         * @return The quaternion defined by the rotation around the axis
         */
        fun fromAngleRadAxis(angle: Float, x: Float, y: Float, z: Float): FloatQuaternion {
            val halfAngle = angle / 2
            val q = (sin(halfAngle.toDouble()) / sqrt(x * x + y * y + z * z)).toFloat()
            return FloatQuaternion(x * q, y * q, z * q, cos(halfAngle))
        }
    }
}

@Serializable
open class BaseFloatQuaternion(
    override val x: Float,
    override val y: Float,
    override val z: Float,
    override val w: Float
) : FloatQuaternion {
    private val hashCode by lazy {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + w.hashCode()
        result
    }

    override val lengthSquared: Float by lazy { x * x + y * y + z * z + w * w }
    override val length: Float by lazy { sqrt(lengthSquared) }

    override val axis: FloatVector3
        get() {
            val q = sqrt(1 - w * w)
            return FloatVector3(x / q, y / q, z / q)
        }

    override fun plus(quaternion: FloatQuaternion): FloatQuaternion =
        add(quaternion.x, quaternion.y, quaternion.z, quaternion.w)

    override fun add(x: Float, y: Float, z: Float, w: Float): FloatQuaternion =
        FloatQuaternion(this.x + x, this.y + y, this.z + z, this.w + w)

    override fun minus(quaternion: FloatQuaternion): FloatQuaternion =
        sub(quaternion.x, quaternion.y, quaternion.z, quaternion.w)

    override fun sub(x: Float, y: Float, z: Float, w: Float): FloatQuaternion =
        FloatQuaternion(this.x - x, this.y - y, this.z - z, this.w - w)

    override fun times(quaternion: FloatQuaternion): FloatQuaternion =
        mul(quaternion.x, quaternion.y, quaternion.z, quaternion.w)

    override fun mul(x: Float, y: Float, z: Float, w: Float): FloatQuaternion =
        FloatQuaternion(
            this.w * x + this.x * w + this.y * z - this.z * y,
            this.w * y + this.y * w + this.z * x - this.x * z,
            this.w * z + this.z * w + this.x * y - this.y * x,
            this.w * w - this.x * x - this.y * y - this.z * z
        )

    override fun div(quaternion: FloatQuaternion): FloatQuaternion =
        div(quaternion.x, quaternion.y, quaternion.z, quaternion.w)

    override fun div(x: Float, y: Float, z: Float, w: Float): FloatQuaternion {
        val d = x * x + y * y + z * z + w * w
        return FloatQuaternion(
            (this.x * w - this.w * x - this.z * y + this.y * z) / d,
            (this.y * w + this.z * x - this.w * y - this.x * z) / d,
            (this.z * w - this.y * x + this.x * y - this.w * z) / d,
            (this.w * w + this.x * x + this.y * y + this.z * z) / d
        )
    }

    override fun dot(quaternion: FloatQuaternion): Float =
        dot(quaternion.x, quaternion.y, quaternion.z, quaternion.w)

    override fun dot(x: Float, y: Float, z: Float, w: Float): Float =
        this.x * x + this.y * y + this.z * z + this.w * w

    override fun rotate(vector: FloatVector3): FloatVector3 =
        rotate(vector.x, vector.y, vector.z)

    override fun rotate(x: Float, y: Float, z: Float): FloatVector3 {
        val length = length
        if (abs(length) < FLT_EPSILON) {
            throw ArithmeticException("Cannot rotate by the zero quaternion")
        }
        val nx = this.x / length
        val ny = this.y / length
        val nz = this.z / length
        val nw = w / length
        val px = nw * x + ny * z - nz * y
        val py = nw * y + nz * x - nx * z
        val pz = nw * z + nx * y - ny * x
        val pw = -nx * x - ny * y - nz * z
        return FloatVector3(
            pw * -nx + px * nw - py * nz + pz * ny,
            pw * -ny + py * nw - pz * nx + px * nz,
            pw * -nz + pz * nw - px * ny + py * nx
        )
    }

    override fun normalize(): FloatImaginary {
        val length = length
        if (abs(length) < FLT_EPSILON) {
            throw ArithmeticException("Cannot normalize the zero quaternion")
        }
        return FloatQuaternion(x / length, y / length, z / length, w / length)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BaseFloatQuaternion

        if (w != other.w) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int = hashCode

    override fun compareTo(other: FloatQuaternion): Int = sign(lengthSquared - other.lengthSquared).toInt()

    override fun toString(): String = "($x, $y, $z, $w)"
}

fun FloatQuaternion(): FloatQuaternion = FloatQuaternion.IDENTITY

fun FloatQuaternion(w: Float): FloatQuaternion = if (w == 0f) FloatQuaternion.ZERO else FloatQuaternion(0f, 0f, 0f, w)

fun FloatQuaternion(x: Float, y: Float, z: Float): FloatQuaternion =
    if (x == 0f && y == 0f && z == 0f) FloatQuaternion.ZERO else FloatQuaternion(x, y, z, 0f)

fun FloatQuaternion(x: Float, y: Float, z: Float, w: Float): FloatQuaternion =
    if (x == 0f && y == 0f && z == 0f && w == 0f) FloatQuaternion.ZERO else BaseFloatQuaternion(x, y, z, w)

fun FloatQuaternion(quaternion: FloatQuaternion): FloatQuaternion =
    FloatQuaternion(quaternion.x, quaternion.y, quaternion.z, quaternion.w)