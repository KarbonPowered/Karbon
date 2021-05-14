package com.karbonpowered.server.event

import com.karbonpowered.server.Session

interface DisconnectingEvent : SessionEvent {
    val session: Session
    val reason: String
    val cause: Throwable?

    override fun call(listener: SessionListener) {
        listener.disconnecting(this)
    }
}

data class DisconnectingEventImpl(
    override val session: Session,
    override val reason: String,
    override val cause: Throwable?
) : DisconnectingEvent