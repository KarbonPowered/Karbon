package com.karbonpowered.server.event

import com.karbonpowered.server.Session

data class DisconnectedEvent(
    override val session: Session,
    val reason: String,
    val cause: Throwable?
) : SessionEvent {
    override fun call(listener: SessionListener) {
        listener.disconnected(this)
    }
}