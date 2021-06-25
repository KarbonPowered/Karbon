package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundKeepAlivePacket(
    val id: Long
) : MinecraftPacket {
    object Codec : PacketCodec<ServerboundKeepAlivePacket> {
        override val packetType: KClass<ServerboundKeepAlivePacket>
            get() = ServerboundKeepAlivePacket::class

        override fun decode(input: Input): ServerboundKeepAlivePacket {
            val id = input.readLong()
            return ServerboundKeepAlivePacket(id)
        }

        override fun encode(output: Output, packet: ServerboundKeepAlivePacket) {
            output.writeLong(packet.id)
        }
    }
}