package com.karbonpowered.api.util

import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.ResourceKeyed

interface ResourceKeyedBuilder<T : ResourceKeyed, B : ResourceKeyedBuilder<T, B>> :
        com.karbonpowered.common.builder.Builder<T, B> {
    fun key(key: ResourceKey): B
}
