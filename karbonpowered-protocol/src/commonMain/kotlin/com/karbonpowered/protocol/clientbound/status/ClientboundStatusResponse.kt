package com.karbonpowered.protocol.clientbound.status

import com.karbonpowered.network.Codec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readString
import com.karbonpowered.protocol.writeString
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundStatusResponse(
    val response: String
) : MinecraftPacket {
    companion object: Codec<ClientboundStatusResponse> {
        override val messageType: KClass<ClientboundStatusResponse>
            get() = ClientboundStatusResponse::class

        override suspend fun decode(input: Input): ClientboundStatusResponse {
            val response = input.readString()
            return ClientboundStatusResponse(response)
        }

        override suspend fun encode(output: Output, message: ClientboundStatusResponse) {
            output.writeString(message.response)
        }
    }
}