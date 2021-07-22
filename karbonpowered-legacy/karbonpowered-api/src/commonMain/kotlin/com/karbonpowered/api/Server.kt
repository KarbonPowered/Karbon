package com.karbonpowered.api

import com.karbonpowered.api.world.server.WorldManager
import com.karbonpowered.audience.ForwardingAudience

interface Server : Engine, ForwardingAudience {
    val worldManager: WorldManager
}