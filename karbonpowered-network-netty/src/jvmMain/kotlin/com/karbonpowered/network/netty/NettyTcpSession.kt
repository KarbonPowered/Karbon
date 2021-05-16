package com.karbonpowered.network.netty

import com.karbonpowered.server.Session
import com.karbonpowered.server.event.*
import com.karbonpowered.server.packet.Packet
import com.karbonpowered.server.packet.PacketProtocol
import io.netty.channel.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import java.net.ConnectException
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import io.netty.channel.Channel as NettyChannel

open class NettyTcpSession(
    override var packetProtocol: PacketProtocol
) : SimpleChannelInboundHandler<Packet>(), Session {
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Default
    private val packetsQueue = Channel<Packet>(Channel.UNLIMITED)
    private var packetHandleJob: Job? = null
    protected var disconnected = false
    override val isConnected: Boolean
        get() = !disconnected
    private val _listeners = CopyOnWriteArrayList<SessionListener>()
    override val listeners: Collection<SessionListener>
        get() = _listeners
    var nettyChannel: NettyChannel? = null

    override fun channelActive(ctx: ChannelHandlerContext) {
        if (disconnected || nettyChannel != null) {
            ctx.channel().close()
            return
        }
        nettyChannel = ctx.channel()
        packetHandleJob = launch {
            packetsQueue.receiveAsFlow().collect { packet ->
                try {
                    callEvent(PacketReceivedEvent(this@NettyTcpSession, packet))
                } catch (t: Throwable) {
                    exceptionCaught(t)
                }
            }
        }
        callEvent(SessionConnectedEvent(this@NettyTcpSession))
    }

    override fun addListener(listener: SessionListener) {
        _listeners.add(listener)
    }

    override fun removeListener(listener: SessionListener) {
        _listeners.remove(listener)
    }

    override fun sendPacket(packet: Packet, flush: Boolean) {
        if (nettyChannel == null) {
            return
        }
        prepareSend(packet)
        if (flush) {
            nettyChannel?.flush()
        }
    }

    override fun sendPackets(vararg packets: Packet, flush: Boolean) {
        if (nettyChannel == null) {
            return
        }
        packets.forEach { packet ->
            prepareSend(packet)
        }
        if (flush) {
            nettyChannel?.flush()
        }
    }

    override fun sendPackets(packets: Iterable<Packet>, flush: Boolean) {
        if (nettyChannel == null) {
            return
        }
        packets.forEach { packet ->
            prepareSend(packet)
        }
        if (flush) {
            nettyChannel?.flush()
        }
    }

    private fun prepareSend(packet: Packet) {
        val sendingEvent = PacketSendingEvent(this, packet)
        callEvent(sendingEvent)

        if (!sendingEvent.isCancelled) {
            val toSend = sendingEvent.packet
            val channelFuture = nettyChannel?.write(toSend)
            channelFuture?.addListener {
                if (channelFuture.isSuccess) {
                    val event = PacketSentEvent(this@NettyTcpSession, packet)
                    callEvent(event)
                } else {
                    exceptionCaught(channelFuture.cause())
                }
            }
        }
    }

    override fun disconnect(reason: String, cause: Throwable?) {
        if (disconnected) {
            return
        }

        disconnected = true

        packetHandleJob?.let {
            it.cancel()
            packetHandleJob = null
        }
        val nettyChannel = nettyChannel.also {
            nettyChannel = null
        }
        if (nettyChannel?.isOpen == true) {
            val disconnectingEvent = DisconnectingEvent(this, reason, cause)
            runBlocking {
                callEvent(disconnectingEvent)
            }
            nettyChannel.flush().close().addListener {
                val disconnectedEvent = DisconnectedEvent(this, reason, cause)
                runBlocking {
                    callEvent(disconnectedEvent)
                }
            }
        }
    }

    override fun callEvent(event: SessionEvent) {
        try {
            listeners.forEach { listener ->
                event.call(listener)
            }
        } catch (t: Throwable) {
            exceptionCaught(t)
        }
    }

    override fun exceptionCaught(cause: Throwable) {
        val message = if (
            cause is ConnectTimeoutException || cause is ConnectException &&
            cause.message?.contains("connection timed out", true) == true
        ) {
            "Connection timed out."
        } else {
            cause.toString()
        }
        disconnect(message, cause)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, packet: Packet) {
        if (packet.isPriority) {
            callEvent(PacketReceivedEvent(this@NettyTcpSession, packet))
        } else {
            launch {
                packetsQueue.send(packet)
            }
        }
    }

    inner class SendFutureListener(
        val packet: Packet
    ) : ChannelFutureListener {
        override fun operationComplete(future: ChannelFuture) {

        }
    }
}