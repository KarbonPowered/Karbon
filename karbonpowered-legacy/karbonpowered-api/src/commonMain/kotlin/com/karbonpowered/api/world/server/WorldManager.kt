package com.karbonpowered.api.world.server

import com.karbonpowered.data.ResourceKey

interface WorldManager {
    val defaultWorld: ServerWorld

    val worlds: Collection<ServerWorld>

    fun world(identifier: ResourceKey): ServerWorld?

    suspend fun loadWorld(identifier: ResourceKey): ServerWorld

    suspend fun unloadWorld(identifier: ResourceKey): Boolean
}