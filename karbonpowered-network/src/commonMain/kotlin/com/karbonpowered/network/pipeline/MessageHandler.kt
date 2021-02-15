package com.karbonpowered.network.pipeline

import com.karbonpowered.network.ConnectionManager
import com.karbonpowered.network.Message
import com.karbonpowered.network.Session
import io.ktor.network.sockets.*
import kotlinx.coroutines.channels.Channel

class MessageHandler(
    val connectionManager: ConnectionManager
) {
    lateinit var session: Session

    fun connectionActive(connection: Connection) {
        this.session = connectionManager.newSession(connection).apply {
            try {
                onReady()
            } catch (e: Throwable) {
                connectionExceptionCaught(e)
            }
        }
    }

    fun connectionInactive(connection: Connection) {
        try {
            session.onDisconnect()
        } catch (e: Throwable) {
            connectionExceptionCaught(e)
        } finally {
            connectionManager.sessionInactivated(session)
        }
    }

    suspend fun connectionReceive(message: Message) = session.messageReceived(message)

    fun connectionExceptionCaught(cause: Throwable) = session.onInboundThrowable(cause)
}