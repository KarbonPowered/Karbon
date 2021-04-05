package com.karbonpowered.api.entity

import com.karbonpowered.api.component.ComponentOwner
import com.karbonpowered.api.tick.Tickable
import com.karbonpowered.api.world.location.Locatable
import com.karbonpowered.math.vector.DoubleVector3

interface Entity<E : Entity<E>> : Locatable, Tickable, ComponentOwner {
    val type: EntityType<E>
    var position: DoubleVector3
}