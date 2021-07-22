package com.karbonpowered.vanilla.protocol

import com.karbonpowered.engine.entity.KarbonEntity
import com.karbonpowered.engine.world.discrete.Transform
import com.karbonpowered.protocol.MinecraftPacket

interface EntityProtocol {
    fun getSpawnPackets(entity: KarbonEntity): Collection<MinecraftPacket>

    fun getDestroyPackets(entity: KarbonEntity): Collection<MinecraftPacket>

    fun getUpdatePackets(entity: KarbonEntity, liveTransform: Transform): Collection<MinecraftPacket>
}