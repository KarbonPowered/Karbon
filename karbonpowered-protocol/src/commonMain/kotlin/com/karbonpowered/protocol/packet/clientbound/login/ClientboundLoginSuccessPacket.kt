package com.karbonpowered.protocol.packet.clientbound.login

import com.karbonpowered.common.UUID
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.*
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundLoginSuccessPacket(
    val uniqueId: UUID,
    val username: String
) : MinecraftPacket {
    companion object : MessageCodec<ClientboundLoginSuccessPacket> {
        override val messageType: KClass<ClientboundLoginSuccessPacket>
            get() = ClientboundLoginSuccessPacket::class

        override suspend fun decode(input: Input): ClientboundLoginSuccessPacket {
            val uniqueId = input.readUUID()
            val username = input.readString()
            return ClientboundLoginSuccessPacket(uniqueId, username)
        }

        override suspend fun encode(output: Output, message: ClientboundLoginSuccessPacket) {
            output.writeUUID(message.uniqueId)
            output.writeString(message.username)
        }
    }
}