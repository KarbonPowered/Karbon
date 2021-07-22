package com.karbonpowered.vanilla.network.protocol.serverbound

import com.karbonpowered.network.PacketCodec
import com.karbonpowered.vanilla.network.protocol.VanillaPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundStatusPingPacket(
    val payload: Long
) : VanillaPacket {
    object Codec : PacketCodec<ServerboundStatusPingPacket> {
        override fun decode(input: Input): ServerboundStatusPingPacket {
            val payload = input.readLong()
            return ServerboundStatusPingPacket(payload)
        }

        override fun encode(output: Output, packet: ServerboundStatusPingPacket) {
            output.writeLong(packet.payload)
        }

        override val packetType: KClass<ServerboundStatusPingPacket>
            get() = ServerboundStatusPingPacket::class
    }
}