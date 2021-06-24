package com.karbonpowered.engine.entity

import com.karbonpowered.common.uuid4
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.component.BaseComponentHolder
import com.karbonpowered.engine.component.NetworkComponent
import com.karbonpowered.engine.component.PhysicsComponent
import com.karbonpowered.engine.snapshot.SnapshotManager
import com.karbonpowered.engine.snapshot.Snapshotable
import com.karbonpowered.engine.snapshot.SnapshotableReference
import com.karbonpowered.engine.world.KarbonRegion
import kotlinx.atomicfu.atomic

open class KarbonEntity(
    val engine: KarbonEngine
) : Snapshotable {
    open val uniqueId = uuid4()
    private val snapshotManager = SnapshotManager()
    private val entityManager = SnapshotableReference(snapshotManager, null)
    val components = BaseComponentHolder(engine)
    var isRemoved by atomic(false)

    init {
        components.add(PhysicsComponent(this))
    }

    val physics get() = components[PhysicsComponent::class]
    open val network get() = components[NetworkComponent::class]

    val region: KarbonRegion? get() = physics.position.region()

    override fun copySnapshot() {
        physics.copySnapshot()
        network.copySnapshot()
    }

    fun preSnapshotRun() {
        network.preSnapshotRun(physics.transformLive.copy())
    }
}