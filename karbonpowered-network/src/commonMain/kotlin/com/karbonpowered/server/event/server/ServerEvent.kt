package com.karbonpowered.server.event.server

import com.karbonpowered.server.Server

interface ServerEvent {
    val server: Server

    fun call(listener: ServerListener)
}