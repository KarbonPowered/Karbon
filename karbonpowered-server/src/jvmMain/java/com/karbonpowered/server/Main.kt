package com.karbonpowered.server

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
import com.karbonpowered.server.cio.TcpServerSettings
import com.karbonpowered.server.cio.cioTcpServer
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    cioTcpServer(TcpServerSettings(protocol = MinecraftProtocol(true) {
        handshake {
            serverbound(0x00, ServerboundHandshakePacket)
        }
        login {
            clientbound(0x02, ClientboundLoginSuccessPacket)
            clientbound(0x04, ClientboundLoginPluginRequestPacket)

            serverbound(0x00, ServerboundLoginStartPacket)
        }
        status {
            clientbound(0x00, ClientboundStatusResponsePacket)
            clientbound(0x01, ClientboundStatusPongPacket)

            serverbound(0x00, ServerboundStatusRequestPacket)
            serverbound(0x01, ServerboundStatusPingPacket)
        }
        game {
            clientbound(0x04, ClientboundSpawnPlayerPacket)
            clientbound(0x08, ClientboundGameBlockBreakingProgressPacket)
            clientbound(0x0E, ClientboundMessagePacket)
            clientbound(0x1F, ClientboundKeepAlivePacket)
            clientbound(0x20, ClientboundPlayChunkData)
            clientbound(0x24, ClientboundGameJoinPacket)
            clientbound(0x34, ClientboundGamePlayerPositionRotationPacket)

            serverbound(0x00, ServerboundAcceptTeleportationPacket)
            serverbound(0x01, ServerboundBlockEntityTagQueryPacket)
            serverbound(0x02, ServerboundChangeDifficultyPacket)
            serverbound(0x03, ServerboundChatPacket)
        }
    }))
}