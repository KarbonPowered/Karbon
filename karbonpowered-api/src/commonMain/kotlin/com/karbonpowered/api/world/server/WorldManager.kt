package com.karbonpowered.api.world.server

import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.Server

interface WorldManager {
    val server: Server

    val defaultWorld: ServerWorld

    val worlds: Collection<ServerWorld>

    fun world(identifier: ResourceKey): ServerWorld?

    suspend fun loadWorld(identifier: ResourceKey): ServerWorld

    suspend fun unloadWorld(identifier: ResourceKey): Boolean
}