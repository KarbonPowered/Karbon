package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.audience.MessageType
import com.karbonpowered.common.UUID
import com.karbonpowered.protocol.MagicValues
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.util.readUUID
import com.karbonpowered.protocol.util.writeUUID
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readString
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeString
import com.karbonpowered.server.writeVarInt
import com.karbonpowered.text.LiteralText
import com.karbonpowered.text.Text
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundMessagePacket(
    val message: Text,
    val packetType: MessageType,
    val sender: UUID
) : MinecraftPacket {
    object Codec : PacketCodec<ClientboundMessagePacket> {
        override val packetType: KClass<ClientboundMessagePacket>
            get() = ClientboundMessagePacket::class

        override fun decode(input: Input): ClientboundMessagePacket {
            val message = input.readString()
            val packetType = MagicValues.key<MessageType>(input.readVarInt())
            val sender = input.readUUID()
            return ClientboundMessagePacket(LiteralText(message), packetType, sender)
        }

        override fun encode(output: Output, packet: ClientboundMessagePacket) {
            output.writeString(packet.message.toString())
            output.writeVarInt(MagicValues.value(packet.packetType))
            output.writeUUID(packet.sender)
        }
    }
}
