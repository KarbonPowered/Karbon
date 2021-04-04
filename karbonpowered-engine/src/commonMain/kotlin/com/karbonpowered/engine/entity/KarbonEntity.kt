package com.karbonpowered.engine.entity

import com.karbonpowered.api.entity.Entity
import com.karbonpowered.api.world.Location
import com.karbonpowered.component.BaseComponentOwner
import com.karbonpowered.component.entity.PhysicsComponent
import com.karbonpowered.engine.scheduler.TickManager
import com.karbonpowered.engine.util.concurrent.snapshotable.Snapshotable
import com.karbonpowered.engine.world.KarbonWorld
import com.karbonpowered.engine.world.server.KarbonServerLocation
import kotlinx.atomicfu.atomic
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
abstract class KarbonEntity<E : Entity<E>> : Entity<E>, BaseComponentOwner(), TickManager, Snapshotable {
    var id = Int.MIN_VALUE
    abstract override val world: KarbonWorld
    private var remove = atomic(false)
    val physics = addComponent(PhysicsComponent())


    override val location: Location<*, *>
        get() {
            val transform = physics.transform
            return KarbonServerLocation(world, transform.position.x, transform.position.y, transform.position.z)
        }

    override suspend fun onTick(duration: Duration) {
        components.forEach {
            it.tick(duration)
        }
    }

    override suspend fun tick(duration: Duration) {
        if (canTick()) {
            onTick(duration)
        }
    }

    fun isRemoved(): Boolean = remove.value

    override fun copySnapshot() {
        physics.copySnapshot()
    }
}