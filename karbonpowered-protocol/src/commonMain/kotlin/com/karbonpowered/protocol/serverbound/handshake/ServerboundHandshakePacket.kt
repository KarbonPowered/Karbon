package com.karbonpowered.protocol.serverbound.handshake

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readString
import com.karbonpowered.protocol.readVarInt
import com.karbonpowered.protocol.writeString
import com.karbonpowered.protocol.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundHandshakePacket(
    val protocolVersion: Int,
    val serverAddress: String,
    val port: Int,
    val nextState: Int
) : MinecraftPacket {
    @OptIn(ExperimentalUnsignedTypes::class)
    companion object : com.karbonpowered.network.Codec<ServerboundHandshakePacket> {
        override val messageType: KClass<ServerboundHandshakePacket>
            get() = ServerboundHandshakePacket::class

        override suspend fun decode(input: Input): ServerboundHandshakePacket {
            val protocolVersion = input.readVarInt()
            val serverAddress = input.readString()
            val port = input.readUShort().toInt()
            val nextState = input.readVarInt()
            return ServerboundHandshakePacket(protocolVersion, serverAddress, port, nextState)
        }

        override suspend fun encode(output: Output, message: ServerboundHandshakePacket) {
            output.writeVarInt(message.protocolVersion)
            output.writeString(message.serverAddress)
            output.writeShort(message.port.toShort())
            output.writeVarInt(message.nextState)
        }
    }
}