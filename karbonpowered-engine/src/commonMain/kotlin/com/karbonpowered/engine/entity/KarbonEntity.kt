package com.karbonpowered.engine.entity

import com.karbonpowered.api.entity.Entity
import com.karbonpowered.component.BaseComponentOwner
import kotlinx.atomicfu.atomic

abstract class KarbonEntity<E : Entity<E>> : Entity<E>, BaseComponentOwner() {
    companion object {
        val ENTITY_ID = atomic(0)
    }
}