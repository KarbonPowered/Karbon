package com.karbonpowered.api

import com.karbonpowered.api.registry.ScopedRegistryHolder

interface Engine : ScopedRegistryHolder {
    val game: Game
}