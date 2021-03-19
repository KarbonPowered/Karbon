package com.karbonpowered.protocol.packet.clientbound.status

import com.karbonpowered.network.Codec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readString
import com.karbonpowered.protocol.writeString
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundStatusResponsePacket(
    val response: String
) : MinecraftPacket {
    companion object : Codec<ClientboundStatusResponsePacket> {
        override val messageType: KClass<ClientboundStatusResponsePacket>
            get() = ClientboundStatusResponsePacket::class

        override suspend fun decode(input: Input): ClientboundStatusResponsePacket {
            val response = input.readString()
            return ClientboundStatusResponsePacket(response)
        }

        override suspend fun encode(output: Output, message: ClientboundStatusResponsePacket) {
            output.writeString(message.response)
        }
    }
}