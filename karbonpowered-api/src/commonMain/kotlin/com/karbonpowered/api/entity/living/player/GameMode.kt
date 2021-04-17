package com.karbonpowered.api.entity.living.player

import com.karbonpowered.api.Karbon
import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.registry.DefaultedRegistryReference
import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.api.registry.RegistryKey
import com.karbonpowered.api.registry.RegistryTypes
import com.karbonpowered.text.TextRepresentable

interface GameMode : DefaultedRegistryValue, TextRepresentable

object GameModes {
    val ADVENTURE by key(ResourceKey.karbon("adventure"))
    val CREATIVE by key(ResourceKey.karbon("creative"))
    val NOT_SET by key(ResourceKey.karbon("not_set"))
    val SPECTATOR by key(ResourceKey.karbon("spectator"))
    val SURVIVAL by key(ResourceKey.karbon("survival"))

    private fun key(resourceKey: ResourceKey): DefaultedRegistryReference<GameMode> =
            RegistryKey(RegistryTypes.GAME_MODE, resourceKey).asDefaultedReference { Karbon.game.registries }
}