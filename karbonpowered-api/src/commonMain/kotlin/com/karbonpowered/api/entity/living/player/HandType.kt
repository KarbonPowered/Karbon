package com.karbonpowered.api.entity.living.player

import com.karbonpowered.api.registry.DefaultedRegistryValue

interface HandType : DefaultedRegistryValue

object HandTypes {
    val MAIN_HAND = object : HandType {}
    val OFF_HAND = object : HandType {}
}