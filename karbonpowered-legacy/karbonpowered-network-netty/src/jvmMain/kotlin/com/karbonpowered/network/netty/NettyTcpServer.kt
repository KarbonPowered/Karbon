package com.karbonpowered.network.netty

import com.karbonpowered.server.AbstractServer
import com.karbonpowered.server.packet.PacketProtocol
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import kotlinx.coroutines.coroutineScope

open class NettyTcpServer(
    override val host: String,
    override val port: Int,
    val protocolProvider: () -> PacketProtocol,
) : AbstractServer(host, port) {
    val eventLoopGroup = Netty.createEventLoopGroup()
    var channel: Channel? = null
    override suspend fun bind() {
        channel = ServerBootstrap().apply {
            channel(Netty.serverSocketChannel())
            childHandler(object : ChannelInitializer<Channel>() {
                override fun initChannel(channel: Channel) {
                    val session = createSession()
                    session.packetProtocol.newServerSession(session)

                    channel.pipeline().addLast("packet-sizer", NettyTcpPacketSizer(session))
                    channel.pipeline().addLast("packet-codec", NettyTcpPacketCodec(session))
                    channel.pipeline().addLast("session", session)
                }
            })
            group(eventLoopGroup)
            localAddress(host, port)
        }.bind().channel()
    }

    override fun createSession(): NettyTcpServerSession {
        val protocol = protocolProvider()
        val session = NettyTcpServerSession(this@NettyTcpServer, protocol)
        return session
    }

    override suspend fun closeImpl(): Unit = coroutineScope {
        val channel = channel
        if (channel?.isOpen == true) {
            channel.close().await()
        }
        eventLoopGroup.shutdownGracefully().await()
    }
}
