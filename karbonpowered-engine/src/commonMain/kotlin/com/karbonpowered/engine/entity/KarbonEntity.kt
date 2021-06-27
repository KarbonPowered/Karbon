package com.karbonpowered.engine.entity

import com.karbonpowered.common.UUID
import com.karbonpowered.common.uuid4
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.component.BaseComponentHolder
import com.karbonpowered.engine.component.ComponentHolder
import com.karbonpowered.engine.snapshot.SnapshotManager
import com.karbonpowered.engine.snapshot.Snapshotable
import com.karbonpowered.engine.snapshot.SnapshotableReference
import com.karbonpowered.engine.util.tick.Tickable
import com.karbonpowered.engine.world.KarbonRegion
import com.karbonpowered.engine.world.LoadOption
import com.karbonpowered.engine.world.discrete.Transform
import kotlinx.atomicfu.atomic
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

open class KarbonEntity(
    val engine: KarbonEngine,
    transform: Transform,
    open val uniqueId: UUID = uuid4()
) : Snapshotable, Tickable {
    private val snapshotManager = SnapshotManager()
    private val entityManager = SnapshotableReference(snapshotManager, null)
    val components: ComponentHolder = BaseComponentHolder(engine)
    val physics = KarbonPhysics(this, transform)
    val observer = EntityObserver(this)
    var isRemoved by atomic(false)
    val world get() = physics.position.world

    val region: KarbonRegion?
        get() = physics.position.region(engine.worldManager)

    suspend fun finalizeRun() {
        val regionLive = physics.position.region(engine.worldManager, LoadOption.NO_LOAD)
        val regionSnapshot = physics.snapshot.value.position.region(engine.worldManager, LoadOption.NO_LOAD)
        //Move entity from Region A to Region B
        if (regionLive != regionSnapshot) {
            regionSnapshot?.entityManager?.removeEntity(this)
            // Add entity
            regionLive?.entityManager?.addEntity(this)
            if (regionSnapshot != null && regionLive != null) {
                physics.crossInto(regionSnapshot, regionLive)
            }
        }
        observer.update()
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun tick(duration: Duration) {
        components.forEach { component ->
            component.tick(duration)
        }
    }

    override fun copySnapshot() {
        physics.copySnapshot()
        observer.copySnapshot()
    }

    fun preSnapshotRun() {
    }
}