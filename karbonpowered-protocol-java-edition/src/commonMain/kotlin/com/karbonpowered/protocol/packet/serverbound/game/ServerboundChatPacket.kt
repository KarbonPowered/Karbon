package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readString
import com.karbonpowered.server.writeString
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundChatPacket(
    val message: String
) : MinecraftPacket {
    companion object : PacketCodec<ServerboundChatPacket> {
        override val packetType: KClass<ServerboundChatPacket>
            get() = ServerboundChatPacket::class

        override fun decode(input: Input): ServerboundChatPacket {
            val message = input.readString()
            return ServerboundChatPacket(message)
        }

        override fun encode(output: Output, packet: ServerboundChatPacket) {
            output.writeString(packet.message)
        }
    }
}
