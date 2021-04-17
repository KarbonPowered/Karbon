package com.karbonpowered.api.network

import com.karbonpowered.api.entity.living.player.server.ServerPlayer

interface ServerPlayerConnection : PlayerConnection, ServerSideConnection {
    override val player: ServerPlayer
    val latency: Int
}