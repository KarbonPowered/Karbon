package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.audience.MessageType
import com.karbonpowered.common.UUID
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.protocol.*
import com.karbonpowered.protocol.util.readUUID
import com.karbonpowered.protocol.util.writeUUID
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
    companion object : PacketCodec<ClientboundMessagePacket> {
        override val packetType: KClass<ClientboundMessagePacket>
            get() = ClientboundMessagePacket::class

        override fun decode(input: Input): ClientboundMessagePacket {
            val message = input.readString()
            val packetType = MagicValues.value<MessageType>(input.readVarInt())
            val sender = input.readUUID()
            return ClientboundMessagePacket(LiteralText(message), packetType, sender)
        }

        override fun encode(output: Output, data: ClientboundMessagePacket) {
            output.writeString(data.message.toString())
            output.writeVarInt(MagicValues.key(data.packetType))
            output.writeUUID(data.sender)
        }
    }
}
