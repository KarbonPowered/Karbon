package com.karbonpowered.server.event.server

import com.karbonpowered.server.Server

data class ServerClosingEvent(
    override val server: Server
) : ServerEvent {
    override fun call(listener: ServerListener) {
        listener.serverClosing(this)
    }
}