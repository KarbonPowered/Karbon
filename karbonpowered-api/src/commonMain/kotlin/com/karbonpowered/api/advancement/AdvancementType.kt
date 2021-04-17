package com.karbonpowered.api.advancement

import com.karbonpowered.api.Karbon
import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.registry.DefaultedRegistryReference
import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.api.registry.RegistryKey
import com.karbonpowered.api.registry.RegistryTypes
import com.karbonpowered.text.TextColor

interface AdvancementType : DefaultedRegistryValue {
    val textColor: TextColor
}

object AdvancementTypes {
    private val CHALLENGE = key(ResourceKey.karbon("challenge"))
    private val GOAL = key(ResourceKey.karbon("goal"))
    private val TASK = key(ResourceKey.karbon("task"))

    private fun key(location: ResourceKey): DefaultedRegistryReference<AdvancementType> =
            RegistryKey.of(RegistryTypes.ADVANCEMENT_TYPE, location).asDefaultedReference { Karbon.game.registries }
}