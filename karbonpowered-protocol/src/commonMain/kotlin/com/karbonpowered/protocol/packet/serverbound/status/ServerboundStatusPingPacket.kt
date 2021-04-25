package com.karbonpowered.protocol.packet.serverbound.status

import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.protocol.MinecraftPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundStatusPingPacket(
    val payload: Long
) : MinecraftPacket {
    companion object : PacketCodec<ServerboundStatusPingPacket> {
        override val packetType: KClass<ServerboundStatusPingPacket>
            get() = ServerboundStatusPingPacket::class

        override fun decode(input: Input): ServerboundStatusPingPacket {
            val payload = input.readLong()
            return ServerboundStatusPingPacket(payload)
        }

        override fun encode(output: Output, message: ServerboundStatusPingPacket) {
            output.writeLong(message.payload)
        }
    }
}