package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readString
import com.karbonpowered.protocol.writeString
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundChatPacket(
    val message: String
) : MinecraftPacket {
    companion object : MessageCodec<ServerboundChatPacket> {
        override val messageType: KClass<ServerboundChatPacket>
            get() = ServerboundChatPacket::class

        override fun decode(input: Input): ServerboundChatPacket {
            val message = input.readString()
            return ServerboundChatPacket(message)
        }

        override fun encode(output: Output, data: ServerboundChatPacket) {
            output.writeString(data.message)
        }
    }
}
