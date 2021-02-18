package com.karbonpowered.protocol.clientbound.status

import com.karbonpowered.network.Codec
import com.karbonpowered.protocol.MinecraftPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundStatusPong(
    val payload: Long
) : MinecraftPacket {
    companion object: Codec<ClientboundStatusPong> {
        override val messageType: KClass<ClientboundStatusPong>
            get() = ClientboundStatusPong::class

        override suspend fun decode(input: Input): ClientboundStatusPong {
            val long = input.readLong()
            return ClientboundStatusPong(long)
        }

        override suspend fun encode(output: Output, message: ClientboundStatusPong) {
            output.writeLong(message.payload)
        }
    }
}