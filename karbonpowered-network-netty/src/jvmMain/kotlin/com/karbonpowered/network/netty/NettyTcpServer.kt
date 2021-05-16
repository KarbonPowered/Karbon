package com.karbonpowered.network.netty

import com.karbonpowered.server.AbstractServer
import com.karbonpowered.server.packet.PacketProtocol
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class NettyTcpServer(
    override val host: String,
    override val port: Int,
    val protocolProvider: () -> PacketProtocol
) : AbstractServer(host, port, protocolProvider) {
    val eventLoopGroup = Netty.createEventLoopGroup()
    var channel: Channel? = null
    override suspend fun bind() {
        channel = ServerBootstrap().apply {
            channel(Netty.serverSocketChannel())
            childHandler(object : ChannelInitializer<Channel>() {
                override fun initChannel(channel: Channel) {
                    val protocol = protocolProvider()
                    val session = NettyTcpServerSession(this@NettyTcpServer, protocol)
                    protocol.newServerSession(session)

                    channel.pipeline().addLast("packet-sizer", NettyTcpPacketSizer(session))
                    channel.pipeline().addLast("packet-codec", NettyTcpPacketCodec(session))
                    channel.pipeline().addLast("session", session)
                }
            })
            group(eventLoopGroup)
            localAddress(host, port)
        }.bind().suspendAwait()
    }

    override suspend fun closeImpl(): Unit = coroutineScope {
        val channel = channel
        if (channel?.isOpen == true) {
            channel.close().suspendAwait()
        }
        eventLoopGroup.shutdownGracefully().suspendAwait()
    }
}