package com.karbonpowered.protocol.packet.serverbound.status

import com.karbonpowered.network.Codec
import com.karbonpowered.protocol.MinecraftPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundStatusPingPacket(
    val payload: Long
) : MinecraftPacket {
    companion object : Codec<ServerboundStatusPingPacket> {
        override val messageType: KClass<ServerboundStatusPingPacket>
            get() = ServerboundStatusPingPacket::class

        override suspend fun decode(input: Input): ServerboundStatusPingPacket {
            val payload = input.readLong()
            return ServerboundStatusPingPacket(payload)
        }

        override suspend fun encode(output: Output, message: ServerboundStatusPingPacket) {
            output.writeLong(message.payload)
        }
    }
}