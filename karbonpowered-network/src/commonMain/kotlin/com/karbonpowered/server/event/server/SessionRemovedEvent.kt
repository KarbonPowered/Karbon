package com.karbonpowered.server.event.server

import com.karbonpowered.server.Server
import com.karbonpowered.server.Session

data class SessionRemovedEvent(
    override val server: Server,
    val session: Session
) : ServerEvent {
    override fun call(listener: ServerListener) {
        listener.sessionRemoved(this)
    }
}