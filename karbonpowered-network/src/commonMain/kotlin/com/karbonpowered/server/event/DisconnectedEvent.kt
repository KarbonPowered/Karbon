package com.karbonpowered.server.event

import com.karbonpowered.server.Session

interface DisconnectedEvent : SessionEvent {
    val session: Session
    val reason: String
    val cause: Throwable?

    override fun call(listener: SessionListener) {
        listener.disconnected(this)
    }
}

data class DisconnectedEventImpl(
    override val session: Session,
    override val reason: String,
    override val cause: Throwable?
) : DisconnectedEvent