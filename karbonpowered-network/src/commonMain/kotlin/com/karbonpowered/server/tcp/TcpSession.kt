package com.karbonpowered.server.tcp

import com.karbonpowered.server.Session
import com.karbonpowered.server.event.*
import com.karbonpowered.server.packet.Packet
import com.karbonpowered.server.packet.PacketProtocol
import com.karbonpowered.server.parsePacket
import com.karbonpowered.server.writePacket
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class TcpSession(
    override val connection: Connection,
    override val coroutineContext: CoroutineContext
) : Session {
    protected var disconnected = false
    private val _listeners = ArrayList<SessionListener>()
    override val listeners: Collection<SessionListener>
        get() = _listeners

    override fun addListener(listener: SessionListener) {
        _listeners.add(listener)
    }

    override fun removeListener(listener: SessionListener) {
        _listeners.remove(listener)
    }

    override fun send(packet: Packet) {
        if (connection.output.isClosedForWrite) {
            return
        }
        val sendingEvent = PacketSendingEventImpl(this, packet)
        callEvent(sendingEvent)

        if (!sendingEvent.isCancelled) {
            val toSend = sendingEvent.packet
            launch {
                writePacket(toSend)
                callEvent(PacketSentEventImpl(this@TcpSession, toSend))
            }
        }
    }

    override fun disconnect(reason: String?, cause: Throwable?) {
        if (disconnected) {
            return
        }

        disconnected = true

        if (isConnected) {
            callEvent(DisconnectingEventImpl(this, reason ?: "Connection closed", cause))
            connection.socket.close()
            launch {
                connection.socket.awaitClosed()
                callEvent(DisconnectedEventImpl(this@TcpSession, reason ?: "Connection closed", cause))
            }
        } else {
            callEvent(DisconnectedEventImpl(this, reason ?: "Connection closed", cause))
        }
    }

    override fun callEvent(event: SessionEvent) {
        try {
            _listeners.forEach {
                event.call(it)
            }
        } catch (t: Throwable) {
            exceptionCaught(t)
        }
    }

    override fun exceptionCaught(cause: Throwable) {
        val message = cause.message.toString()
        disconnect(message, cause)
    }
}

class TcpServerSession(
    connection: Connection,
    override var packetProtocol: PacketProtocol,
    coroutineContext: CoroutineContext
) : TcpSession(connection, coroutineContext) {
    suspend fun startChannelRead() = launch {
        try {
            callEvent(ConnectedEventImpl(this@TcpServerSession))
            while (true) {
                val packet = parsePacket()
                callEvent(PacketReceivedEventImpl(this@TcpServerSession, packet))
            }
        } catch (cause: Throwable) {
            exceptionCaught(cause)
        }
    }
}