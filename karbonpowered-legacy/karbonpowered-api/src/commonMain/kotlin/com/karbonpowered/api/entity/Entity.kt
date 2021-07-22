package com.karbonpowered.api.entity

import com.karbonpowered.api.world.location.Locatable
import com.karbonpowered.math.vector.DoubleVector3

interface Entity<E : Entity<E>> : Locatable {
    val type: EntityType<E>
    var position: DoubleVector3
}