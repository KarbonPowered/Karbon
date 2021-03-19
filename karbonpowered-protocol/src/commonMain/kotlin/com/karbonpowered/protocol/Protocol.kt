package com.karbonpowered.protocol

import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusPong
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusResponse
import com.karbonpowered.protocol.packet.serverbound.handshake.ServerboundHandshakePacket
import com.karbonpowered.protocol.packet.serverbound.login.ServerboundLoginStartPacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusPing
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusRequest

object HandshakeProtocol : MinecraftProtocol("handshake") {
    init {
        bind(0x00, ServerboundHandshakePacket::class, ServerboundHandshakePacket)
    }
}

object ServerboundLoginProtocol : MinecraftProtocol("server-login") {
    init {
        bind(0x00, ServerboundLoginStartPacket::class, ServerboundLoginStartPacket)
    }
}

object ClientboundStatusProtocol : MinecraftProtocol("client-status") {
    init {
        bind(0x00, ClientboundStatusResponse::class, ClientboundStatusResponse)
        bind(0x01, ClientboundStatusPong::class, ClientboundStatusPong)
    }
}

object ServerboundStatusRequest : MinecraftProtocol("server-status") {
    init {
        bind(0x00, ServerboundStatusRequest::class, ServerboundStatusRequest)
        bind(0x01, ServerboundStatusPing::class, ServerboundStatusPing)
    }
}