package com.karbonpowered.engine.protocol

import com.karbonpowered.server.event.SessionEvent
import com.karbonpowered.server.event.SessionListener

interface ProtocolEvent : SessionEvent {
    override fun call(listener: SessionListener) {
        (listener as? ProtocolEventListener)?.onProtocolEvent(this)
    }
}