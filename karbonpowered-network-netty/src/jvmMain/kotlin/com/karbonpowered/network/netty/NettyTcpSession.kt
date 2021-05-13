package com.karbonpowered.network.netty

import com.karbonpowered.server.Session
import com.karbonpowered.server.event.*
import com.karbonpowered.server.packet.Packet
import com.karbonpowered.server.packet.PacketProtocol
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.Channel as NettyChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.coroutines.CoroutineContext

class NettyTcpSession : SimpleChannelInboundHandler<Packet>(), Session {
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Default
    private val packetsQueue = Channel<Packet>(Channel.UNLIMITED)
    lateinit var packetHandleJob: Job
    protected var disconnected = false
    override val isConnected: Boolean
        get() = !disconnected
    override val packetProtocol: PacketProtocol
        get() = TODO("Not yet implemented")
    private val _listeners = CopyOnWriteArrayList<SessionListener>()
    override val listeners: Collection<SessionListener>
        get() = _listeners
    var nettyChannel: NettyChannel? = null

    override fun channelActive(ctx: ChannelHandlerContext) {
        if(disconnected || nettyChannel != null) {
            ctx.channel().close()
            return
        }
        nettyChannel = ctx.channel()
        packetHandleJob = launch {
            packetsQueue.receiveAsFlow().collect { packet ->
                try {
                    callEvent(PacketReceivedEventImpl(this@NettyTcpSession, packet))
                } catch (t: Throwable) {
                    exceptionCaught(t)
                }
            }
        }
        callEvent(ConnectedEventImpl(this))
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
        val sendingEvent = PacketSendingEventImpl(this, packet)
        callEvent(sendingEvent)

        if(!sendingEvent.isCancelled) {
            val toSend = sendingEvent.packet
            nettyChannel?.write(toSend)?.addListener(SendFutureListener(toSend))
        }
    }

    override fun disconnect(reason: String, cause: Throwable?) {
        if(disconnected) {
            return
        }

        disconnected = true
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
        TODO("Not yet implemented")
    }

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: Packet?) {
        TODO("Not yet implemented")
    }

    inner class SendFutureListener(
        val packet: Packet
    ) : ChannelFutureListener {
        override fun operationComplete(future: ChannelFuture) {
            if (future.isSuccess) {
                val event = PacketSentEventImpl(this@NettyTcpSession, packet)
                callEvent(event)
            } else {
                exceptionCaught(future.cause())
            }
        }
    }
}