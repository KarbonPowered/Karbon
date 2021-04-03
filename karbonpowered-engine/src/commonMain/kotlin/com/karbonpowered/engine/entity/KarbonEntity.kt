package com.karbonpowered.engine.entity

import com.karbonpowered.api.entity.Entity
import com.karbonpowered.component.BaseComponentOwner
import com.karbonpowered.engine.scheduler.TickManager
import kotlinx.atomicfu.atomic
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
abstract class KarbonEntity<E : Entity<E>> : Entity<E>, BaseComponentOwner(), TickManager {
    companion object {
        val ENTITY_ID = atomic(0)
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
}