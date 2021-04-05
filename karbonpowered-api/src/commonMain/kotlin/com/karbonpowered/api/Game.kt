package com.karbonpowered.api

import com.karbonpowered.api.registry.BuilderProvider
import com.karbonpowered.api.registry.FactoryProvider
import com.karbonpowered.api.registry.ScopedRegistryHolder

interface Game : ScopedRegistryHolder {
    val factoryProvider: FactoryProvider
    val builderProvider: BuilderProvider
}