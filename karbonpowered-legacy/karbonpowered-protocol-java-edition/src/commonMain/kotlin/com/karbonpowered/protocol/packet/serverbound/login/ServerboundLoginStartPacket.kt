package com.karbonpowered.protocol.packet.serverbound.login

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.readString
import com.karbonpowered.server.writeString
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundLoginStartPacket(
    val username: String
) : MinecraftPacket {
    object Codec : com.karbonpowered.server.packet.PacketCodec<ServerboundLoginStartPacket> {
        override val packetType: KClass<ServerboundLoginStartPacket>
            get() = ServerboundLoginStartPacket::class

        override fun decode(input: Input): ServerboundLoginStartPacket {
            val username = input.readString(16)
            return ServerboundLoginStartPacket(username)
        }

        override fun encode(output: Output, packet: ServerboundLoginStartPacket) {
            output.writeString(packet.username)
        }
    }
}