package com.karbonpowered.engine.network

import com.karbonpowered.engine.network.handler.HandshakeHandler
import com.karbonpowered.engine.network.handler.LoginStartHandler
import com.karbonpowered.engine.network.handler.StatusPingHandler
import com.karbonpowered.engine.network.handler.StatusRequestHandler
import com.karbonpowered.protocol.MinecraftProtocol
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundGameJoinPacket
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundGamePlayerPositionRotationPacket
import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginPluginRequestPacket
import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginSuccessPacket
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusPongPacket
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusResponsePacket
import com.karbonpowered.protocol.packet.serverbound.handshake.ServerboundHandshakePacket
import com.karbonpowered.protocol.packet.serverbound.login.ServerboundLoginStartPacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusPingPacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusRequestPacket

val MinecraftProtocol.Companion.PROTOCOL_VERSION: Int get() = 0x40000000 + 20

class HandshakeProtocol(isServer: Boolean) : MinecraftProtocol("handshake", isServer) {
    init {
        serverbound(0x00, ServerboundHandshakePacket::class, ServerboundHandshakePacket, HandshakeHandler)
    }
}

class LoginProtocol(isServer: Boolean) : MinecraftProtocol("login", isServer) {
    init {
        serverbound(0x00, ServerboundLoginStartPacket::class, ServerboundLoginStartPacket, LoginStartHandler)

        clientbound(0x02, ClientboundLoginSuccessPacket::class, ClientboundLoginSuccessPacket)
        clientbound(0x04, ClientboundLoginPluginRequestPacket::class, ClientboundLoginPluginRequestPacket)
    }
}

class StatusProtocol(isServer: Boolean) : MinecraftProtocol("status", isServer) {
    init {
        clientbound(0x00, ClientboundStatusResponsePacket::class, ClientboundStatusResponsePacket)
        clientbound(0x01, ClientboundStatusPongPacket::class, ClientboundStatusPongPacket)

        serverbound(0x00, ServerboundStatusRequestPacket::class, ServerboundStatusRequestPacket, StatusRequestHandler)
        serverbound(0x01, ServerboundStatusPingPacket::class, ServerboundStatusPingPacket, StatusPingHandler)
    }
}

class GameProtocol(isServer: Boolean) : MinecraftProtocol("game", isServer) {
    init {
        clientbound(0x26, ClientboundGameJoinPacket::class, ClientboundGameJoinPacket)
        clientbound(0x37, ClientboundGamePlayerPositionRotationPacket::class, ClientboundGamePlayerPositionRotationPacket)
    }
}