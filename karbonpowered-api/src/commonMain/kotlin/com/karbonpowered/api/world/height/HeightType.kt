package com.karbonpowered.api.world.height

import com.karbonpowered.api.Karbon
import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.registry.DefaultedRegistryReference
import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.api.registry.RegistryKey
import com.karbonpowered.api.registry.RegistryTypes

interface HeightType : DefaultedRegistryValue

object HeightTypes {
    val MOTION_BLOCKING = key(ResourceKey.karbon("motion_blocking"))
    val MOTION_BLOCKING_NO_LEAVES = key(ResourceKey.karbon("motion_blocking_no_leaves"))
    val OCEAN_FLOOR = key(ResourceKey.karbon("ocean_floor"))
    val OCEAN_FLOOR_WG = key(ResourceKey.karbon("ocean_floor_wg"))
    val WORLD_SURFACE = key(ResourceKey.karbon("world_surface"))
    val WORLD_SURFACE_WG = key(ResourceKey.karbon("world_surface_wg"))

    private fun key(resourceKey: ResourceKey): DefaultedRegistryReference<HeightType> =
            RegistryKey(RegistryTypes.HEIGHT_TYPE, resourceKey).asDefaultedReference { Karbon.game.registries }
}