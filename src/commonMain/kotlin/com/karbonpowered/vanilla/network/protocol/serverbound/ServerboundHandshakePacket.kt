package com.karbonpowered.vanilla.network.protocol.serverbound

import com.karbonpowered.network.*
import com.karbonpowered.vanilla.network.protocol.VanillaPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundHandshakePacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val port: Short,
    val nextState: Int
) : VanillaPacket {
    object Codec : PacketCodec<ServerboundHandshakePacket> {
        override fun encode(output: Output, packet: ServerboundHandshakePacket) {
            output.writeVarInt(packet.protocolVersion)
            output.writeString(packet.serverAddress)
            output.writeShort(packet.port)
            output.writeVarInt(packet.nextState)
        }

        override fun decode(input: Input): ServerboundHandshakePacket {
            val protocolVersion = input.readVarInt()
            val serverAddress = input.readString()
            val port = input.readShort()
            val nextState = input.readVarInt()
            return ServerboundHandshakePacket(protocolVersion, serverAddress, port, nextState)
        }

        override val packetType: KClass<ServerboundHandshakePacket>
            get() = ServerboundHandshakePacket::class
    }
}