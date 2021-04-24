package com.karbonpowered.api.util

import com.karbonpowered.data.ResourceKey
import com.karbonpowered.data.ResourceKeyed

interface ResourceKeyedBuilder<T : ResourceKeyed, B : ResourceKeyedBuilder<T, B>> :
    com.karbonpowered.common.builder.Builder<T, B> {
    fun key(key: ResourceKey): B
}
