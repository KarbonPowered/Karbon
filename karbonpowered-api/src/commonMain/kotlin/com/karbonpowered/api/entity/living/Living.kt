package com.karbonpowered.api.entity.living

import com.karbonpowered.api.entity.Entity

interface Living<E : Living<E>> : Entity<E> {
    var health: Double
}