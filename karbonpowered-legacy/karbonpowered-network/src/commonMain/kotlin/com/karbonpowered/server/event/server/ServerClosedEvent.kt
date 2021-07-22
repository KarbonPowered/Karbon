package com.karbonpowered.server.event.server

import com.karbonpowered.server.Server

data class ServerClosedEvent(
    override val server: Server
) : ServerEvent {
    override fun call(listener: ServerListener) {
        listener.serverClosed(this)
    }
}