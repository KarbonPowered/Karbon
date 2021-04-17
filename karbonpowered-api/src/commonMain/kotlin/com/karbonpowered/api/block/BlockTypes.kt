package com.karbonpowered.api.block

import com.karbonpowered.api.Karbon
import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.registry.DefaultedRegistryReference
import com.karbonpowered.api.registry.RegistryKey
import com.karbonpowered.api.registry.RegistryTypes

object BlockTypes {
    val AIR = key(ResourceKey.minecraft("air"))

    private fun key(resourceKey: ResourceKey): DefaultedRegistryReference<BlockType> =
            RegistryKey(RegistryTypes.BLOCK_TYPE, resourceKey).asDefaultedReference { Karbon.game.registries }
}