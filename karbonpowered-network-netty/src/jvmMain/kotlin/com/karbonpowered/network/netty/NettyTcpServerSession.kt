package com.karbonpowered.network.netty

import com.karbonpowered.server.packet.PacketProtocol

class NettyTcpServerSession(
    val server: NettyTcpServer,
    protocol: PacketProtocol
) : NettyTcpSession(protocol) {

}