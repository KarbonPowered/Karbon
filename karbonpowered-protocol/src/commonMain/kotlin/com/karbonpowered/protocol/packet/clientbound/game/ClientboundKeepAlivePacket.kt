package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.protocol.MinecraftPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundKeepAlivePacket(
    val id: Long
) : MinecraftPacket {
    companion object : PacketCodec<ClientboundKeepAlivePacket> {
        override val packetType: KClass<ClientboundKeepAlivePacket>
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