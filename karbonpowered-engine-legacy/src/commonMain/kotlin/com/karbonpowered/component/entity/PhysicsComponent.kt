package com.karbonpowered.component.entity

import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.math.vector.FloatVector3
import com.karbonpowered.physics.Transform
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class PhysicsComponent : EntityComponent() {
    private val live = Transform()
    private val snapshot = Transform()

    var transform: Transform
        get() = snapshot.copy()
        set(value) {
            setTransform(value)
        }

    var position: DoubleVector3
        get() = snapshot.position
        set(value) {
            live.position = value
        }

    var rotation: FloatVector3
        get() = snapshot.rotation
        set(value) {
            live.rotation = value
        }

    fun setTransform(transform: Transform, sync: Boolean = true): PhysicsComponent = apply {
        live.set(transform)
        if (sync) {
            sync()
        }
    }

    fun isTransformDirty(): Boolean = snapshot != live

    @OptIn(ExperimentalTime::class)
    override suspend fun tick(duration: Duration) {
    }

    private fun sync() {
        (owner as? KarbonPlayer)?.network?.forceSync()
    }

    fun copySnapshot() {
        snapshot.set(live)
    }
}