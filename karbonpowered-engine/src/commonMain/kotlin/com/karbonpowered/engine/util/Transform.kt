package com.karbonpowered.engine.util

import com.karbonpowered.common.concurrent.dualLock
import com.karbonpowered.engine.world.KarbonWorld
import com.karbonpowered.engine.world.Position
import com.karbonpowered.math.imaginary.FloatQuaternion
import com.karbonpowered.math.vector.FloatVector3
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

class Transform(
    position: Position = Position.INVALID,
    rotation: FloatQuaternion = FloatQuaternion.IDENTITY,
    scale: FloatVector3 = FloatVector3.ONE
) {
    constructor(transform: Transform) : this() {
        set(transform)
    }

    private val lock = reentrantLock()
    private var _position: Position = position
    private var _rotation: FloatQuaternion = rotation
    private var _scale: FloatVector3 = scale

    var position: Position
        get() = lock.withLock {
            _position
        }
        set(value) = lock.withLock {
            _position = value
        }

    var rotation: FloatQuaternion
        get() = lock.withLock {
            _rotation
        }
        set(value) = lock.withLock {
            _rotation = value
        }

    var scale: FloatVector3
        get() = lock.withLock {
            _scale
        }
        set(value) = lock.withLock {
            _scale = value
        }

    fun translate(offset: FloatVector3) = apply {
        lock.withLock {
            _position += offset
        }
    }

    fun rotate(offset: FloatQuaternion) = apply {
        lock.withLock {
            _rotation *= offset
        }
    }

    fun scale(offset: FloatVector3) = apply {
        lock.withLock {
            _scale += offset
        }
    }

    fun set(transform: Transform) = apply {
        dualLock(lock, transform.lock) {
            setUnsafe(transform._position, transform._rotation, transform._scale)
        }
    }

    fun set(
        world: KarbonWorld,
        positionX: Float = 0f,
        positionY: Float = 0f,
        positionZ: Float = 0f,
        rotationX: Float = 0f,
        rotationY: Float = 0f,
        rotationZ: Float = 0f,
        rotationW: Float = 0f,
        scaleX: Float = 0f,
        scaleY: Float = 0f,
        scaleZ: Float = 0f
    ) = set(
        Position(world, positionX, positionY, positionZ),
        FloatQuaternion(rotationX, rotationY, rotationZ, rotationW),
        FloatVector3(scaleX, scaleY, scaleZ)
    )

    fun set(position: Position, rotation: FloatQuaternion, scale: FloatVector3) = apply {
        lock.withLock {
            setUnsafe(position, rotation, scale)
        }
    }

    private fun setUnsafe(position: Position, rotation: FloatQuaternion, scale: FloatVector3) {
        _position = position
        _rotation = rotation
        _scale = scale
    }

    override fun toString(): String = buildString {
        append(this@Transform::class.simpleName)
        append("(")
        lock.withLock {
            append(_position)
            append(", ")
            append(_rotation)
            append(", ")
            append(_scale)
        }
        append(")")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Transform

        return dualLock(lock, other.lock) {
            if (_position != other._position) return@dualLock false
            if (_rotation != other._rotation) return@dualLock false
            return@dualLock _scale == other._scale
        }
    }

    override fun hashCode(): Int {
        var result = 0
        lock.withLock {
            result = _position.hashCode()
            result = 31 * result + _rotation.hashCode()
            result = 31 * result + _scale.hashCode()
        }
        return result
    }

    fun isEmpty(): Boolean = lock.withLock {
        _position == Position.INVALID && _rotation == FloatQuaternion.IDENTITY && _scale == FloatVector3.ONE
    }

    fun copy(): Transform = Transform(this)
}