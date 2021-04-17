package com.karbonpowered.api.world.difficulty

import com.karbonpowered.api.Karbon
import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.registry.DefaultedRegistryReference
import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.api.registry.RegistryKey
import com.karbonpowered.api.registry.RegistryTypes

interface Difficulty : DefaultedRegistryValue

object Difficulties {
    val EASY by key(ResourceKey.karbon("easy"))
    val HARD by key(ResourceKey.karbon("hard"))
    val NORMAL by key(ResourceKey.karbon("normal"))
    val PEACEFUL by key(ResourceKey.karbon("peaceful"))

    private fun key(resourceKey: ResourceKey): DefaultedRegistryReference<Difficulty> =
            RegistryKey(RegistryTypes.DIFFICULTY, resourceKey).asDefaultedReference { Karbon.game.registries }
}