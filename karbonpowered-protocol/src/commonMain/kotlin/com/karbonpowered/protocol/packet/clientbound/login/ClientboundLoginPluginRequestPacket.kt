package com.karbonpowered.protocol.packet.clientbound.login

import com.karbonpowered.api.Identifier
import com.karbonpowered.network.Codec
import com.karbonpowered.protocol.*
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundLoginPluginRequestPacket(
    val messageId: Int,
    val identifier: Identifier,
    val data: ByteReadPacket
) : MinecraftPacket {
    companion object : Codec<ClientboundLoginPluginRequestPacket> {
        override suspend fun decode(input: Input): ClientboundLoginPluginRequestPacket {
            val messageId = input.readVarInt()
            val identifier = Identifier(input.readString())
            val data = buildPacket {
                while (!input.endOfInput) {
                    writeByte(input.readByte())
                }
            }
            return ClientboundLoginPluginRequestPacket(messageId, identifier, data)
        }

        override suspend fun encode(output: Output, message: ClientboundLoginPluginRequestPacket) {
            output.writeVarInt(message.messageId)
            output.writeString(message.identifier.toString())
            output.writePacket(message.data)
        }

        override val messageType: KClass<ClientboundLoginPluginRequestPacket>
            get() = ClientboundLoginPluginRequestPacket::class
    }
}