package com.karbonpowered.protocol.packet.clientbound.status

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundStatusPongPacket(
    val payload: Long
) : MinecraftPacket {
    companion object : MessageCodec<ClientboundStatusPongPacket> {
        override val messageType: KClass<ClientboundStatusPongPacket>
            get() = ClientboundStatusPongPacket::class

        override fun decode(input: Input): ClientboundStatusPongPacket {
            val long = input.readLong()
            return ClientboundStatusPongPacket(long)
        }

        override fun encode(output: Output, message: ClientboundStatusPongPacket) {
            output.writeLong(message.payload)
        }
    }
}