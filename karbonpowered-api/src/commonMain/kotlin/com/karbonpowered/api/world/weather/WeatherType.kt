package com.karbonpowered.api.world.weather

import com.karbonpowered.api.Karbon
import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.registry.DefaultedRegistryReference
import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.api.registry.RegistryKey
import com.karbonpowered.api.registry.RegistryTypes

interface WeatherType : DefaultedRegistryValue

object WeatherTypes {
    val CLEAR = key(ResourceKey.karbon("clear"))
    val RAIN = key(ResourceKey.karbon("rain"))
    val THUNDER = key(ResourceKey.karbon("thunder"))

    private fun key(resourceKey: ResourceKey): DefaultedRegistryReference<WeatherType> =
            RegistryKey(RegistryTypes.WEATHER_TYPE, resourceKey).asDefaultedReference { Karbon.game.registries }
}