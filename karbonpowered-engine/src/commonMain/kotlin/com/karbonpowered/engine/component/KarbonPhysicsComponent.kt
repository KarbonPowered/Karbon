package com.karbonpowered.engine.component

import com.karbonpowered.api.component.getComponent
import com.karbonpowered.component.entity.PhysicsComponent
import com.karbonpowered.component.entity.PlayerNetworkComponent
import com.karbonpowered.math.Transform
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

class KarbonPhysicsComponent : PhysicsComponent() {
    private val lock = reentrantLock()
    private lateinit var snapshot: Transform
    private lateinit var live: Transform

    override val transform: Transform
        get() = lock.withLock {
            snapshot.copy()
        }

    val isTransformDirty: Boolean get() = lock.withLock { snapshot != live }

    fun setTransform(transform: Transform, sync: Boolean = true) {
        lock.withLock {
            this.live = transform
        }
        if (sync) {
            sync()
        }
    }

    fun sync() {
        owner.getComponent<PlayerNetworkComponent>()?.forceSync()
    }

}