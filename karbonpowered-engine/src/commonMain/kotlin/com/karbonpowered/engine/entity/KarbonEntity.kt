package com.karbonpowered.engine.entity

import com.karbonpowered.common.uuid4
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.component.BaseComponentHolder
import com.karbonpowered.engine.component.NetworkComponent
import com.karbonpowered.engine.component.PhysicsComponent
import com.karbonpowered.engine.snapshot.SnapshotManager
import com.karbonpowered.engine.snapshot.Snapshotable
import com.karbonpowered.engine.snapshot.SnapshotableReference
import com.karbonpowered.engine.util.Transform
import com.karbonpowered.engine.world.KarbonRegion
import kotlinx.atomicfu.atomic

open class KarbonEntity(
    val engine: KarbonEngine,
    transform: Transform? = null
) : Snapshotable {
    open val uniqueId = uuid4()
    private val snapshotManager = SnapshotManager()
    private val entityManager = SnapshotableReference(snapshotManager, null)
    val components = BaseComponentHolder(engine)
    var isRemoved by atomic(false)

    val physics get() = components[PhysicsComponent::class, { PhysicsComponent(this) }]
    open val network get() = components[NetworkComponent::class, { NetworkComponent(this) }]

    init {
        if (transform != null) {
            physics.setTransform(transform, false)
            physics.copySnapshot()
        }
    }

    val region: KarbonRegion?
        get() = physics.position.region()

    override fun copySnapshot() {
        physics.copySnapshot()
        network.copySnapshot()
    }

    fun preSnapshotRun() {
        network.preSnapshotRun(physics.transformLive.copy())
    }
}