package com.karbonpowered.server.event.server

import com.karbonpowered.server.Server
import com.karbonpowered.server.Session
import com.karbonpowered.server.event.server.ServerEvent
import com.karbonpowered.server.event.server.ServerListener

data class SessionAddedEvent(
    override val server: Server,
    val session: Session
) : ServerEvent {
    override fun call(listener: ServerListener) {
        listener.sessionAdded(this)
    }
}