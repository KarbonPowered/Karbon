package com.karbonpowered.server.packet

import com.karbonpowered.server.Session
import kotlin.reflect.KClass

abstract class PacketProtocol {
    val incoming = mutableMapOf<Int, PacketCodec<out Packet>>()
    val outgoing = mutableMapOf<PacketCodec<out Packet>, Int>()
    val codecs = mutableMapOf<KClass<out Packet>, PacketCodec<out Packet>>()

    val packetHeader: PacketHeader = DefaultPacketHeader()

    abstract fun newServerSession(session: Session)

    fun clearPackets() {
        incoming.clear()
        outgoing.clear()
        codecs.clear()
    }

    fun registerIncoming(id: Int, codec: PacketCodec<out Packet>) {
        incoming[id] = codec
    }

    fun registerOutgoing(id: Int, codec: PacketCodec<out Packet>) {
        outgoing[codec] = id
        codecs[codec.packetType] = codec
    }

    fun outgoingId(codec: PacketCodec<out Packet>) = outgoing[codec]
        ?: throw IllegalArgumentException("Unregistered outgoing packet: ${codec.packetType}")

    fun <T : Packet> outgoingId(packetType: KClass<T>): Int = outgoingId(outgoingCodec(packetType))

    @Suppress("UNCHECKED_CAST")
    fun <T : Packet> outgoingCodec(packetType: KClass<T>): PacketCodec<T> = codecs[packetType] as? PacketCodec<T>
        ?: throw IllegalArgumentException("Unregistered outgoing packet: $packetType")

    fun incomingCodec(packetId: Int): PacketCodec<out Packet> =
        incoming[packetId] ?: throw IllegalArgumentException(
            "Unregistered incoming packet with id: $packetId (0x${packetId.hex()})"
        )

    private fun Int.hex() = toString(16).uppercase().padStart(2, '0')
}