package com.karbonpowered.vanilla

import com.karbonpowered.api.MinecraftVersion
import com.karbonpowered.protocol.MinecraftProtocol
import com.karbonpowered.protocol.packet.clientbound.game.*
import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginDisconnect
import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginPluginRequestPacket
import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginSuccessPacket
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusPongPacket
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusResponsePacket
import com.karbonpowered.protocol.packet.serverbound.game.*
import com.karbonpowered.protocol.packet.serverbound.handshake.ServerboundHandshakePacket
import com.karbonpowered.protocol.packet.serverbound.login.ServerboundLoginStartPacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusPingPacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusRequestPacket

class VanillaProtocol(
    version: MinecraftVersion,
    isServer: Boolean
) : MinecraftProtocol(version, isServer) {
    init {
        handshake {
            serverbound(0x00, ServerboundHandshakePacket.Codec)
        }
        status {
            clientbound(0x00, ClientboundStatusResponsePacket.Codec)
            clientbound(0x01, ClientboundStatusPongPacket.Codec)

            serverbound(0x00, ServerboundStatusRequestPacket.Codec)
            serverbound(0x01, ServerboundStatusPingPacket.Codec)
        }
        login {
            clientbound(0x00, ClientboundLoginDisconnect.Codec)
            clientbound(0x02, ClientboundLoginSuccessPacket.Codec)
            clientbound(0x04, ClientboundLoginPluginRequestPacket.Codec)

            serverbound(0x00, ServerboundLoginStartPacket.Codec)
        }
        game {
            clientbound(0x04, ClientboundSpawnPlayerPacket.Codec)
            clientbound(0x09, ClientboundGameBlockBreakingProgressPacket.Codec)
            clientbound(0x0F, ClientboundMessagePacket.Codec)
            clientbound(0x21, ClientboundKeepAlivePacket.Codec)
            clientbound(0x22, ClientboundPlayColumnData.Codec)
            clientbound(0x26, ClientboundGameJoinPacket.Codec)
            clientbound(0x38, ClientboundGamePlayerPositionRotationPacket.Codec)
            clientbound(0x49, ClientboundSyncPositionPacket.Codec)
            clientbound(0x4A, ClientboundSyncDistancePacket.Codec)

            serverbound(0x00, ServerboundAcceptTeleportationPacket.Codec)
            serverbound(0x01, ServerboundBlockEntityTagQueryPacket.Codec)
            serverbound(0x02, ServerboundChangeDifficultyPacket.Codec)
            serverbound(0x03, ServerboundChatPacket.Codec)
            serverbound(0x04, ServerboundClientRequestPacket.Codec)
            serverbound(0x05, ServerboundClientSettingsPacket.Codec)
            serverbound(0x0F, ServerboundKeepAlivePacket.Codec)
            serverbound(0x11, ServerboundPlayerPositionPacket.Codec)
            serverbound(0x12, ServerboundPlayerPositionRotationPacket.Codec)
            serverbound(0x13, ServerboundPlayerRotationPacket.Codec)
            serverbound(0x14, ServerboundPlayerOnGroundPacket.Codec)
            serverbound(0x1B, ServerboundPlayerStatePacket.Codec)
        }
    }
}