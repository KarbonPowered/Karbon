package com.karbonpowered.server

import com.karbonpowered.server.event.server.ServerListener

interface Server {
    val host: String
    val port: Int

    val sessions: Collection<Session>
    val listeners: Collection<ServerListener>
    fun addListener(listener: ServerListener)
    fun removeListener(listener: ServerListener)

    suspend fun bind()

    suspend fun close()
}