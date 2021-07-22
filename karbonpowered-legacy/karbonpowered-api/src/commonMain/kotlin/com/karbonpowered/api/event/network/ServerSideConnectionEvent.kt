package com.karbonpowered.api.event.network

import com.karbonpowered.api.entity.living.player.server.ServerPlayer
import com.karbonpowered.api.event.Event

interface ServerSideConnectionEvent : Event {
    interface Join {
        val player: ServerPlayer
    }
}