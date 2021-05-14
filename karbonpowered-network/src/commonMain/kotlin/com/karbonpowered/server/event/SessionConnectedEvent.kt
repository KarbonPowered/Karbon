package com.karbonpowered.server.event

import com.karbonpowered.server.Session


data class SessionConnectedEvent(
    override val session: Session
) : SessionEvent {
    override fun call(listener: SessionListener) {
        listener.connected(this)
    }
}