package com.karbonpowered.protocol.packet.serverbound.handshake

import com.karbonpowered.protocol.*
import com.karbonpowered.server.readString
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeString
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundHandshakePacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val port: Int,
    val handshakeIntent: MinecraftProtocol.SubProtocol
) : MinecraftPacket {
    @OptIn(ExperimentalUnsignedTypes::class)
    companion object : com.karbonpowered.server.packet.PacketCodec<ServerboundHandshakePacket> {
        override val packetType: KClass<ServerboundHandshakePacket>
            get() = ServerboundHandshakePacket::class

        override fun decode(input: Input): ServerboundHandshakePacket {
            val protocolVersion = input.readVarInt()
            val serverAddress = input.readString()
            val port = input.readUShort().toInt()
            val nextState = MagicValues.key<MinecraftProtocol.SubProtocol>(input.readVarInt())
            return ServerboundHandshakePacket(protocolVersion, serverAddress, port, nextState)
        }

        override fun encode(output: Output, message: ServerboundHandshakePacket) {
            output.writeVarInt(message.protocolVersion)
            output.writeString(message.serverAddress)
            output.writeShort(message.port.toShort())
            output.writeVarInt(MagicValues.value(message.handshakeIntent))
        }
    }
}