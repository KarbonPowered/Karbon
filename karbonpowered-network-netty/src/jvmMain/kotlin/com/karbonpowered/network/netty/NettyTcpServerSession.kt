package com.karbonpowered.network.netty

import com.karbonpowered.server.packet.PacketProtocol
import io.netty.channel.ChannelHandlerContext

class NettyTcpServerSession(
    val server: NettyTcpServer,
    protocol: PacketProtocol
) : NettyTcpSession(protocol) {
    override fun toString(): String = nettyChannel?.remoteAddress().toString()

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        server.addSession(this)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        server.removeSession(this)
    }
}