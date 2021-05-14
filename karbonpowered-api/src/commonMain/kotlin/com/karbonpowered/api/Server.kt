package com.karbonpowered.api

import com.karbonpowered.audience.ForwardingAudience
import com.karbonpowered.api.world.server.WorldManager

interface Server : Engine, ForwardingAudience {
    val worldManager: WorldManager
}