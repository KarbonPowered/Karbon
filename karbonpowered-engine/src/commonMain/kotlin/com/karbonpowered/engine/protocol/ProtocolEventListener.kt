package com.karbonpowered.engine.protocol

import com.karbonpowered.engine.protocol.event.UpdateEntityEvent
import com.karbonpowered.server.event.SessionListener

interface ProtocolEventListener : SessionListener {
    fun onProtocolEvent(event: ProtocolEvent) {}
    fun onUpdateEntity(event: UpdateEntityEvent) {}
}