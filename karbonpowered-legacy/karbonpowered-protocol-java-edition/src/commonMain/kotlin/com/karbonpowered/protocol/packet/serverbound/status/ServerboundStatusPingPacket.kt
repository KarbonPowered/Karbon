package com.karbonpowered.protocol.packet.serverbound.status

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundStatusPingPacket(
    val payload: Long
) : MinecraftPacket {
    object Codec : PacketCodec<ServerboundStatusPingPacket> {
        override val packetType: KClass<ServerboundStatusPingPacket>
            get() = ServerboundStatusPingPacket::class

        override fun decode(input: Input): ServerboundStatusPingPacket {
            val payload = input.readLong()
            return ServerboundStatusPingPacket(payload)
        }

        override fun encode(output: Output, packet: ServerboundStatusPingPacket) {
            output.writeLong(packet.payload)
        }
    }
}