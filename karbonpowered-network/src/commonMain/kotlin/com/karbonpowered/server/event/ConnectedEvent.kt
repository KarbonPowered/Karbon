package com.karbonpowered.server.event

import com.karbonpowered.server.Session

interface ConnectedEvent : SessionEvent {
    val session: Session

    override fun call(listener: SessionListener) {
        listener.connected(this)
    }
}

data class ConnectedEventImpl(override val session: Session) : ConnectedEvent