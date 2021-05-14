package com.karbonpowered.data

import com.karbonpowered.common.builder.Builder

interface ResourceKeyedBuilder<T:ResourceKeyed,B:ResourceKeyedBuilder<T,B>>:Builder<T,B> {
    var key: ResourceKey
    fun key(key: ResourceKey): B = apply {
        this.key = key
    } as B
}