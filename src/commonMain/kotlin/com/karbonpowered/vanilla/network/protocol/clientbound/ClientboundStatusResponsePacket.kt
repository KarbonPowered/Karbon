package com.karbonpowered.vanilla.network.protocol.clientbound

import com.karbonpowered.network.PacketCodec
import com.karbonpowered.network.readString
import com.karbonpowered.network.writeString
import com.karbonpowered.vanilla.network.protocol.VanillaPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundStatusResponsePacket(
    val response: String
) : VanillaPacket {
    object Codec : PacketCodec<ClientboundStatusResponsePacket> {
        override fun encode(output: Output, packet: ClientboundStatusResponsePacket) {
            output.writeString(packet.response)
        }

        override fun decode(input: Input): ClientboundStatusResponsePacket {
            val response = input.readString()
            return ClientboundStatusResponsePacket(response)
        }

        override val packetType: KClass<ClientboundStatusResponsePacket>
            get() = ClientboundStatusResponsePacket::class
    }
}