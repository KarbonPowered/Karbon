package com.karbonpowered.engine.protocol

import com.karbonpowered.server.event.SessionListener

interface ProtocolEventListener : SessionListener {
    fun onProtocolEvent(event: ProtocolEvent) {}
}