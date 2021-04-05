package com.karbonpowered.api.world.weather

import com.karbonpowered.api.Identifier
import com.karbonpowered.api.Karbon
import com.karbonpowered.api.registry.DefaultedRegistryReference
import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.api.registry.RegistryKey
import com.karbonpowered.api.registry.RegistryTypes

interface WeatherType : DefaultedRegistryValue

object WeatherTypes {
    val CLEAR = key(Identifier.karbon("clear"))
    val RAIN = key(Identifier.karbon("rain"))
    val THUNDER = key(Identifier.karbon("thunder"))

    private fun key(identifier: Identifier): DefaultedRegistryReference<WeatherType> =
            RegistryKey(RegistryTypes.WEATHER_TYPE, identifier).asDefaultedReference { Karbon.game.registries }
}