package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.api.audience.MessageType
import com.karbonpowered.common.UUID
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.*
import com.karbonpowered.text.LiteralText
import com.karbonpowered.text.Text
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundMessagePacket(
        val message: Text,
        val messageType: MessageType,
        val sender: UUID
) : MinecraftPacket {
    companion object : MessageCodec<ClientboundMessagePacket> {
        override val messageType: KClass<ClientboundMessagePacket>
            get() = ClientboundMessagePacket::class

        override fun decode(input: Input): ClientboundMessagePacket {
            val message = input.readString()
            val messageType = MagicValues.value<MessageType>(input.readVarInt())
            val sender = input.readUUID()
            return ClientboundMessagePacket(LiteralText(message), messageType, sender)
        }

        override fun encode(output: Output, data: ClientboundMessagePacket) {
            output.writeString(data.message.toString())
            output.writeVarInt(MagicValues.key(data.messageType))
            output.writeUUID(data.sender)
        }
    }
}
