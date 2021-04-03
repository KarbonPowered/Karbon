package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundKeepAlivePacket(
    val id: Long
) : MinecraftPacket {
    companion object : MessageCodec<ClientboundKeepAlivePacket> {
        override val messageType: KClass<ClientboundKeepAlivePacket>
            get() = ClientboundKeepAlivePacket::class

        override fun decode(input: Input): ClientboundKeepAlivePacket {
            val id = input.readLong()
            return ClientboundKeepAlivePacket(id)
        }

        override fun encode(output: Output, data: ClientboundKeepAlivePacket) {
            output.writeLong(data.id)
        }
    }
}