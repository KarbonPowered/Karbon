package com.karbonpowered.engine.network

import com.karbonpowered.engine.network.handler.*
import com.karbonpowered.network.Message
import com.karbonpowered.network.MessageHandler
import com.karbonpowered.network.Session
import com.karbonpowered.protocol.MinecraftProtocol
import com.karbonpowered.protocol.packet.clientbound.game.*
import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginPluginRequestPacket
import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginSuccessPacket
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusPongPacket
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusResponsePacket
import com.karbonpowered.protocol.packet.serverbound.game.*
import com.karbonpowered.protocol.packet.serverbound.handshake.ServerboundHandshakePacket
import com.karbonpowered.protocol.packet.serverbound.login.ServerboundLoginStartPacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusPingPacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusRequestPacket

val PRINT_PACKET_HANDLER: (Session, Message) -> Unit = { session, message ->
    println("$session -> $message")
}

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
        clientbound(0x04, ClientboundSpawnPlayerPacket::class, ClientboundSpawnPlayerPacket)
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
        clientbound(0x4B, ClientboundScoreboardDisplayPacket::class, ClientboundScoreboardDisplayPacket)
        clientbound(0x52, ClientboundSetScoreboardObjectivePacket::class, ClientboundSetScoreboardObjectivePacket)
        clientbound(0x54, ClientboundSetPlayerTeamPacket::class, ClientboundSetPlayerTeamPacket)
        clientbound(0x55, ClientboundGameScoreboardScorePacket::class, ClientboundGameScoreboardScorePacket)

        serverbound(0x00, ServerboundAcceptTeleportationPacket::class, ServerboundAcceptTeleportationPacket, PRINT_PACKET_HANDLER)
        serverbound(0x01, ServerboundBlockEntityTagQueryPacket::class, ServerboundBlockEntityTagQueryPacket, PRINT_PACKET_HANDLER)
        serverbound(0x02, ServerboundChangeDifficultyPacket::class, ServerboundChangeDifficultyPacket, PRINT_PACKET_HANDLER)
        serverbound(0x03, ServerboundChatPacket::class, ServerboundChatPacket, ChatHandler)
        serverbound(0x04, ServerboundClientCommandPacket::class, ServerboundClientCommandPacket, PRINT_PACKET_HANDLER)
        serverbound(0x05, ServerboundClientInformationPacket::class, ServerboundClientInformationPacket, PRINT_PACKET_HANDLER)
        serverbound(0x06, ServerboundCommandSuggestionPacket::class, ServerboundCommandSuggestionPacket, PRINT_PACKET_HANDLER)
        serverbound(0x07, ServerboundContainerButtonClickPacket::class, ServerboundContainerButtonClickPacket, PRINT_PACKET_HANDLER)

        serverbound(17, ServerboundPlayerPositionPacket::class, ServerboundPlayerPositionPacket, PRINT_PACKET_HANDLER)
        serverbound(18, ServerboundPlayerPositionRotationPacket::class, ServerboundPlayerPositionRotationPacket, PRINT_PACKET_HANDLER)
        serverbound(19, ServerboundPlayerRotationPacket::class, ServerboundPlayerRotationPacket, PRINT_PACKET_HANDLER)
        serverbound(20, ServerboundPlayerOnGroundPacket::class, ServerboundPlayerOnGroundPacket, PRINT_PACKET_HANDLER)
    }
}