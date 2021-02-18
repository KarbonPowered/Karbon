package com.karbonpowered.protocol.serverbound.status

import com.karbonpowered.network.Codec
import com.karbonpowered.protocol.MinecraftPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundStatusPing(
    val payload: Long
) : MinecraftPacket {
    companion object : Codec<ServerboundStatusPing> {
        override val messageType: KClass<ServerboundStatusPing>
            get() = ServerboundStatusPing::class

        override suspend fun decode(input: Input): ServerboundStatusPing {
            val payload = input.readLong()
            return ServerboundStatusPing(payload)
        }

        override suspend fun encode(output: Output, message: ServerboundStatusPing) {
            output.writeLong(message.payload)
        }
    }
}