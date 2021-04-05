package com.karbonpowered.api.world.height

import com.karbonpowered.api.Identifier
import com.karbonpowered.api.Karbon
import com.karbonpowered.api.registry.DefaultedRegistryReference
import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.api.registry.RegistryKey
import com.karbonpowered.api.registry.RegistryTypes

interface HeightType : DefaultedRegistryValue

object HeightTypes {
    val MOTION_BLOCKING = key(Identifier.karbon("motion_blocking"))
    val MOTION_BLOCKING_NO_LEAVES = key(Identifier.karbon("motion_blocking_no_leaves"))
    val OCEAN_FLOOR = key(Identifier.karbon("ocean_floor"))
    val OCEAN_FLOOR_WG = key(Identifier.karbon("ocean_floor_wg"))
    val WORLD_SURFACE = key(Identifier.karbon("world_surface"))
    val WORLD_SURFACE_WG = key(Identifier.karbon("world_surface_wg"))

    private fun key(identifier: Identifier): DefaultedRegistryReference<HeightType> =
            RegistryKey(RegistryTypes.HEIGHT_TYPE, identifier).asDefaultedReference { Karbon.game.registries }
}