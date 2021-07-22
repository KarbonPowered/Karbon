package com.karbonpowered.api.world.chunk

import com.karbonpowered.api.registry.DefaultedRegistryValue

interface ChunkState : DefaultedRegistryValue {
    fun isAfter(state: ChunkState): Boolean
}