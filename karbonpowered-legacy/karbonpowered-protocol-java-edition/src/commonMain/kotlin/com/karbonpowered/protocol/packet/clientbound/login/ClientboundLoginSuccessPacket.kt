package com.karbonpowered.protocol.packet.clientbound.login

import com.karbonpowered.common.UUID
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.util.readUUID
import com.karbonpowered.protocol.util.writeUUID
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readString
import com.karbonpowered.server.writeString
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundLoginSuccessPacket(
    val uniqueId: UUID,
    val username: String
) : MinecraftPacket {
    object Codec : PacketCodec<ClientboundLoginSuccessPacket> {
        override val packetType: KClass<ClientboundLoginSuccessPacket>
            get() = ClientboundLoginSuccessPacket::class

        override fun decode(input: Input): ClientboundLoginSuccessPacket {
            val uniqueId = input.readUUID()
            val username = input.readString()
            return ClientboundLoginSuccessPacket(uniqueId, username)
        }

        override fun encode(output: Output, packet: ClientboundLoginSuccessPacket) {
            output.writeUUID(packet.uniqueId)
            output.writeString(packet.username)
        }
    }
}