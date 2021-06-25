package com.karbonpowered.protocol.packet.serverbound.handshake

import com.karbonpowered.protocol.MagicValues
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.MinecraftProtocol
import com.karbonpowered.server.packet.PacketCodec
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
    override val isPriority: Boolean = true

    object Codec : PacketCodec<ServerboundHandshakePacket> {
        override val packetType: KClass<ServerboundHandshakePacket>
            get() = ServerboundHandshakePacket::class

        override fun decode(input: Input): ServerboundHandshakePacket {
            val protocolVersion = input.readVarInt()
            val serverAddress = input.readString()
            val port = input.readUShort().toInt()
            val nextState = MagicValues.key<MinecraftProtocol.SubProtocol>(input.readVarInt())
            return ServerboundHandshakePacket(protocolVersion, serverAddress, port, nextState)
        }

        override fun encode(output: Output, packet: ServerboundHandshakePacket) {
            output.writeVarInt(packet.protocolVersion)
            output.writeString(packet.serverAddress)
            output.writeShort(packet.port.toShort())
            output.writeVarInt(MagicValues.value(packet.handshakeIntent))
        }
    }
}