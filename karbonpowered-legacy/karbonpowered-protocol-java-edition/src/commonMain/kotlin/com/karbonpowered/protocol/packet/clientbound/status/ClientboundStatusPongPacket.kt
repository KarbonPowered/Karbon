package com.karbonpowered.protocol.packet.clientbound.status

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundStatusPongPacket(
    val payload: Long
) : MinecraftPacket {
    object Codec : PacketCodec<ClientboundStatusPongPacket> {
        override val packetType: KClass<ClientboundStatusPongPacket>
            get() = ClientboundStatusPongPacket::class

        override fun decode(input: Input): ClientboundStatusPongPacket {
            val long = input.readLong()
            return ClientboundStatusPongPacket(long)
        }

        override fun encode(output: Output, packet: ClientboundStatusPongPacket) {
            output.writeLong(packet.payload)
        }
    }
}