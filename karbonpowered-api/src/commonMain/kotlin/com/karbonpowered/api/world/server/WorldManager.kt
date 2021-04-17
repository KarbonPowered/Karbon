package com.karbonpowered.api.world.server

import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.Server
import com.karbonpowered.api.world.server.storage.ServerWorldProperties

interface WorldManager {
    val server: Server
    val defaultWorld: ServerWorld
    val worlds: Collection<ServerWorld>
    val worldKeys: Collection<ResourceKey>
    val offlineWorldKeys: Collection<ResourceKey>
        get() = worldKeys.filter { world(it) == null }

    fun world(resourceKey: ResourceKey): ServerWorld?

    fun worldExists(resourceKey: ResourceKey): Boolean

    suspend fun loadWorld(template: WorldTemplate): ServerWorld

    suspend fun loadWorld(resourceKey: ResourceKey): ServerWorld

    suspend fun unloadWorld(resourceKey: ResourceKey): Boolean

    suspend fun unloadWorld(world: ServerWorld): Boolean

    fun templateExists(resourceKey: ResourceKey): Boolean

    suspend fun loadTemplate(resourceKey: ResourceKey): WorldTemplate?

    suspend fun saveTemplate(template: WorldTemplate): Boolean

    suspend fun loadProperties(key: ResourceKey): ServerWorldProperties?

    suspend fun saveProperties(properties: ServerWorldProperties): Boolean

    suspend fun copyWorld(key: ResourceKey, copyKey: ResourceKey): Boolean

    suspend fun moveWorld(key: ResourceKey, moveKey: ResourceKey): Boolean

    suspend fun deleteWorld(key: ResourceKey): Boolean
}