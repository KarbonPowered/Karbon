package com.karbonpowered.minecraft.api.entity

import com.karbonpowered.minecraft.api.registry.DefaultedRegistryValue

interface EntityType<A : Entity<A>> : DefaultedRegistryValue {

}