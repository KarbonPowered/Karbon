package com.karbonpowered.vanilla.network.protocol.clientbound

import com.karbonpowered.network.PacketCodec
import com.karbonpowered.vanilla.network.protocol.VanillaPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundStatusPongPacket(
    val payload: Long
) : VanillaPacket {
    object Codec : PacketCodec<ClientboundStatusPongPacket> {
        override fun encode(output: Output, packet: ClientboundStatusPongPacket) {
            output.writeLong(packet.payload)
        }

        override fun decode(input: Input): ClientboundStatusPongPacket {
            val payload = input.readLong()
            return ClientboundStatusPongPacket(payload)
        }

        override val packetType: KClass<ClientboundStatusPongPacket>
            get() = ClientboundStatusPongPacket::class
    }
}