package com.karbonpowered.component.entity

import com.karbonpowered.api.entity.Entity
import com.karbonpowered.api.component.Component
import com.karbonpowered.api.component.ComponentOwner

abstract class EntityComponent : Component {
    override lateinit var owner: Entity<*>

    override fun attachTo(owner: ComponentOwner): Boolean {
        check(owner is Entity<*>) { "EntityComponent may only be attached to entities" }
        this.owner = owner
        return true
    }
}