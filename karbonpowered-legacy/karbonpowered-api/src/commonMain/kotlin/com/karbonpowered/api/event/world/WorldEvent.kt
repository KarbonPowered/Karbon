package com.karbonpowered.api.event.world

import com.karbonpowered.api.event.Event
import com.karbonpowered.api.world.server.ServerWorld

interface WorldEvent : Event {
    val world: ServerWorld
}