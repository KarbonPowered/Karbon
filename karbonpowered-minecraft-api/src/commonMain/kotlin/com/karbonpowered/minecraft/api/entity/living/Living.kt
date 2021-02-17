package com.karbonpowered.minecraft.api.entity.living

import com.karbonpowered.minecraft.api.entity.Entity

interface Living<E : Living<E>> : Entity<E> {
    var health: Double
}