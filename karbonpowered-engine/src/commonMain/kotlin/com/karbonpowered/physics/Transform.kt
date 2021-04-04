package com.karbonpowered.physics

import com.karbonpowered.common.concurrent.dualLock
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.math.vector.FloatVector3
import com.karbonpowered.math.vector.doubleVector3of
import com.karbonpowered.math.vector.floatVector3of
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

class Transform(
        position: DoubleVector3 = doubleVector3of(),
        rotation: FloatVector3 = floatVector3of()
) {
    private val lock = reentrantLock()

    private var _position = position
    private var _rotation = rotation

    var position: DoubleVector3
        get() = lock.withLock {
            this._position
        }
        set(value) {
            lock.withLock {
                this._position = value
            }
        }

    var rotation: FloatVector3
        get() = lock.withLock {
            this._rotation
        }
        set(value) {
            lock.withLock {
                this._rotation = value
            }
        }

    fun set(transform: Transform) {
        dualLock(lock, transform.lock) {
            _position = transform.position
            _rotation = transform.rotation
        }
    }

    fun isEmpty(): Boolean = lock.withLock {
        _position.x == 0.0 && _position.y == 0.0 && _position.z == 0.0 && _rotation.x == 0f && _rotation.y == 0f && _rotation.z == 0f
    }

    fun copy(): Transform = lock.withLock {
        Transform(_position, _rotation)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Transform

        return dualLock(lock, other.lock) {
            if (_position != other._position) return@dualLock false
            if (_rotation != other._rotation) return@dualLock false
            true
        }
    }

    override fun hashCode(): Int = lock.withLock {
        var result = _position.hashCode()
        result = 31 * result + _rotation.hashCode()
        return result
    }

    override fun toString(): String = "Transform(position=$_position, rotation=$_rotation)"
}