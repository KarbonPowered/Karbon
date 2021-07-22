package com.karbonpowered.vanilla.network

import com.karbonpowered.network.PacketCodec
import com.karbonpowered.vanilla.network.protocol.VanillaPacket
import com.karbonpowered.vanilla.network.protocol.clientbound.ClientboundStatusPongPacket
import com.karbonpowered.vanilla.network.protocol.clientbound.ClientboundStatusResponsePacket
import com.karbonpowered.vanilla.network.protocol.serverbound.ServerboundHandshakePacket
import com.karbonpowered.vanilla.network.protocol.serverbound.ServerboundStatusPingPacket
import com.karbonpowered.vanilla.network.protocol.serverbound.ServerboundStatusRequestPacket
import kotlin.reflect.KClass

val VanillaProtocol756
    get() = VanillaProtocol {
        handshake {
            serverbound(0x00, ServerboundHandshakePacket.Codec)
        }
        status {
            clientbound(0x00, ClientboundStatusResponsePacket.Codec)
            clientbound(0x01, ClientboundStatusPongPacket.Codec)

            serverbound(0x00, ServerboundStatusRequestPacket.Codec)
            serverbound(0x01, ServerboundStatusPingPacket.Codec)
        }
//    login {
//        clientbound(0x00, ClientboundLoginDisconnect.Codec)
//        clientbound(0x02, ClientboundLoginSuccessPacket.Codec)
//        clientbound(0x04, ClientboundLoginPluginRequestPacket.Codec)
//
//        serverbound(0x00, ServerboundLoginStartPacket.Codec)
//    }
//    game {
//        clientbound(0x04, ClientboundSpawnPlayerPacket.Codec)
//        clientbound(0x09, ClientboundGameBlockBreakingProgressPacket.Codec)
//        clientbound(0x0F, ClientboundMessagePacket.Codec)
//        clientbound(0x1D, ClientboundUnloadColumnPacket.Codec)
//        clientbound(0x21, ClientboundKeepAlivePacket.Codec)
//        clientbound(0x22, ClientboundColumnDataPacket.Codec)
//        clientbound(0x26, ClientboundGameJoinPacket.Codec)
//        clientbound(0x38, ClientboundGamePlayerPositionRotationPacket.Codec)
//        clientbound(0x49, ClientboundSyncPositionPacket.Codec)
//        clientbound(0x4A, ClientboundSyncDistancePacket.Codec)
//
//        serverbound(0x00, ServerboundAcceptTeleportationPacket.Codec)
//        serverbound(0x01, ServerboundBlockEntityTagQueryPacket.Codec)
//        serverbound(0x02, ServerboundChangeDifficultyPacket.Codec)
//        serverbound(0x03, ServerboundChatPacket.Codec)
//        serverbound(0x04, ServerboundClientRequestPacket.Codec)
//        serverbound(0x05, ServerboundClientSettingsPacket.Codec)
//        serverbound(0x0F, ServerboundKeepAlivePacket.Codec)
//        serverbound(0x19, ServerboundPlayerAbilitiesPacket.Codec)
//        serverbound(0x11, ServerboundPlayerPositionPacket.Codec)
//        serverbound(0x12, ServerboundPlayerPositionRotationPacket.Codec)
//        serverbound(0x13, ServerboundPlayerRotationPacket.Codec)
//        serverbound(0x14, ServerboundPlayerOnGroundPacket.Codec)
//        serverbound(0x1B, ServerboundPlayerStatePacket.Codec)
//    }
    }

abstract class VanillaProtocol(
    val isServer: Boolean = true
) {
    private val incoming = mutableMapOf<Int, PacketCodec<out VanillaPacket>>()
    private val outgoing = mutableMapOf<PacketCodec<out VanillaPacket>, Int>()
    private val codecs = mutableMapOf<KClass<out VanillaPacket>, PacketCodec<out VanillaPacket>>()

    private lateinit var handshakeBuilder: (ProtocolBuilder) -> Unit
    private lateinit var statusBuilder: (ProtocolBuilder) -> Unit
    private lateinit var loginBuilder: (ProtocolBuilder) -> Unit
    private lateinit var gameBuilder: (ProtocolBuilder) -> Unit

    var state: ProtocolState = ProtocolState.HANDSHAKE
        set(value) {
            field = value
            when (value) {
                ProtocolState.HANDSHAKE -> handshakeBuilder(ProtocolBuilder())
                ProtocolState.STATUS -> statusBuilder(ProtocolBuilder())
                ProtocolState.LOGIN -> loginBuilder(ProtocolBuilder())
                ProtocolState.GAME -> gameBuilder(ProtocolBuilder())
            }
        }

    fun registerIncoming(id: Int, codec: PacketCodec<out VanillaPacket>) {
        incoming[id] = codec
    }

    fun registerOutgoing(id: Int, codec: PacketCodec<out VanillaPacket>) {
        outgoing[codec] = id
        codecs[codec.packetType] = codec
    }

    fun outgoingId(codec: PacketCodec<out VanillaPacket>) = outgoing[codec]
        ?: throw IllegalArgumentException("Unregistered outgoing packet: ${codec.packetType}")

    fun <T : VanillaPacket> outgoingId(packetType: KClass<T>): Int = outgoingId(outgoingCodec(packetType))

    @Suppress("UNCHECKED_CAST")
    fun outgoingCodec(packetType: KClass<out Any>): PacketCodec<out VanillaPacket> = codecs[packetType]
        ?: throw IllegalArgumentException("Unregistered outgoing packet: $packetType")

    fun incomingCodec(packetId: Int): PacketCodec<out VanillaPacket> =
        incoming[packetId] ?: throw IllegalArgumentException(
            "Unregistered incoming packet with id: $packetId (0x${packetId.hex()})"
        )

    fun clearCodecs() {
        incoming.clear()
        outgoing.clear()
        codecs.clear()
    }

    fun handshake(block: ProtocolBuilder.() -> Unit) {
        handshakeBuilder = block
    }

    fun status(block: ProtocolBuilder.() -> Unit) {
        statusBuilder = block
    }

    fun login(block: ProtocolBuilder.() -> Unit) {
        loginBuilder = block
    }

    fun game(block: ProtocolBuilder.() -> Unit) {
        gameBuilder = block
    }

    enum class ProtocolState {
        HANDSHAKE,
        STATUS,
        LOGIN,
        GAME;

        companion object {
            private val values = values()

            operator fun get(state: Int): ProtocolState = values[state]
        }
    }

    inner class ProtocolBuilder internal constructor() {
        fun clientbound(id: Int, codec: PacketCodec<out VanillaPacket>) {
            if (isServer) {
                registerOutgoing(id, codec)
            } else {
                registerIncoming(id, codec)
            }
        }

        fun serverbound(id: Int, codec: PacketCodec<out VanillaPacket>) {
            if (isServer) {
                registerIncoming(id, codec)
            } else {
                registerOutgoing(id, codec)
            }
        }
    }
}

fun VanillaProtocol(isServer: Boolean = true, build: VanillaProtocol.() -> Unit) =
    object : VanillaProtocol(isServer) {}.apply(build)

private fun Int.hex() = toString(16).uppercase().padStart(2, '0')