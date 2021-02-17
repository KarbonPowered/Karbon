package com.karbonpowered.minecraft.api.entity

interface Entity<E : Entity<E>> {
    val type: EntityType<E>
}