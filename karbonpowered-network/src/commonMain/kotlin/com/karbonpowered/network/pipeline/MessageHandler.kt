package com.karbonpowered.network.pipeline

import com.karbonpowered.network.ConnectionManager
import com.karbonpowered.network.Message
import com.karbonpowered.network.Session
import io.ktor.network.sockets.*
import kotlinx.atomicfu.atomic

class MessageHandler(
    val connectionManager: ConnectionManager
) {
    var session by atomic<Session?>(null)

    fun connectionActive(connection: Connection) {
        this.session = connectionManager.newSession(connection).apply {
            onReady()
        }
    }

    fun connectionInactive(connection: Connection) {
        session?.apply {
            onDisconnect()
            connectionManager.sessionInactivated(this)
        }
    }

    suspend fun connectionRead(connection: Connection, message: Message) {
        session?.messageReceived(message)
    }

    suspend fun connectionExceptionCaught(connection: Connection, cause: Throwable) {
        session?.onInboundThrowable(cause)
    }
}