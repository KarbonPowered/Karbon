package com.karbonpowered.protocol.packet.clientbound.login

import com.karbonpowered.data.ResourceKey
import com.karbonpowered.data.ResourceKeyImpl
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readString
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeString
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundLoginPluginRequestPacket(
    val messageId: Int,
    val identifier: ResourceKey,
    val data: ByteReadPacket
) : MinecraftPacket {
    companion object : PacketCodec<ClientboundLoginPluginRequestPacket> {
        override val packetType: KClass<ClientboundLoginPluginRequestPacket>
            get() = ClientboundLoginPluginRequestPacket::class

        override fun decode(input: Input): ClientboundLoginPluginRequestPacket {
            val messageId = input.readVarInt()
            val (namespace, value) = input.readString().split(":")
            val identifier = ResourceKeyImpl(namespace, value)
            val data = buildPacket {
                while (!input.endOfInput) {
                    writeByte(input.readByte())
                }
            }
            return ClientboundLoginPluginRequestPacket(messageId, identifier, data)
        }

        override fun encode(output: Output, packet: ClientboundLoginPluginRequestPacket) {
            output.writeVarInt(packet.messageId)
            output.writeString(packet.identifier.toString())
            output.writePacket(packet.data)
        }
    }
}