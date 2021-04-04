package com.karbonpowered.engine.network

import com.karbonpowered.engine.network.handler.*
import com.karbonpowered.protocol.MinecraftProtocol
import com.karbonpowered.protocol.packet.clientbound.game.*
import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginPluginRequestPacket
import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginSuccessPacket
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusPongPacket
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusResponsePacket
import com.karbonpowered.protocol.packet.serverbound.game.ServerboundAdvancementTabPacket
import com.karbonpowered.protocol.packet.serverbound.game.ServerboundChatPacket
import com.karbonpowered.protocol.packet.serverbound.handshake.ServerboundHandshakePacket
import com.karbonpowered.protocol.packet.serverbound.login.ServerboundLoginStartPacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusPingPacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusRequestPacket

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
        clientbound(0x0f, ClientboundMessagePacket::class, ClientboundMessagePacket)
        clientbound(0x21, ClientboundKeepAlivePacket::class, ClientboundKeepAlivePacket)
        clientbound(0x22, ClientboundPlayChunkData::class, ClientboundPlayChunkData)
        clientbound(0x26, ClientboundGameJoinPacket::class, ClientboundGameJoinPacket)
        clientbound(
            0x37,
            ClientboundGamePlayerPositionRotationPacket::class,
            ClientboundGamePlayerPositionRotationPacket
        )
        clientbound(0x35, ClientboundGamePlayerListPacket::class, ClientboundGamePlayerListPacket)
        clientbound(0x09, ClientboundGameBlockBreakingProgressPacket::class, ClientboundGameBlockBreakingProgressPacket)
        clientbound(0x4C, ClientboundScoreboardDisplayPacket::class, ClientboundScoreboardDisplayPacket)
        clientbound(0x53, ClientboundSetScoreboardObjectivePacket::class, ClientboundSetScoreboardObjectivePacket)
        clientbound(0x55, ClientboundSetPlayerTeamPacket::class, ClientboundSetPlayerTeamPacket)

        serverbound(0x03, ServerboundChatPacket::class, ServerboundChatPacket, ChatHandler)
//        serverbound(0x3C, ServerboundAdvancementTabPacket::class, ServerboundAdvancementTabPacket) TODO
    }
}