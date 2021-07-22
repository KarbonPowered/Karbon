package com.karbonpowered.vanilla.network.protocol.serverbound

import com.karbonpowered.network.PacketCodec
import com.karbonpowered.vanilla.network.protocol.VanillaPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

object ServerboundStatusRequestPacket : VanillaPacket {
    override fun toString(): String = "ServerboundStatusRequestPacket"

    override fun equals(other: Any?): Boolean = super.equals(other)

    override fun hashCode(): Int = super.hashCode()

    object Codec : PacketCodec<ServerboundStatusRequestPacket> {
        override val packetType: KClass<ServerboundStatusRequestPacket>
            get() = ServerboundStatusRequestPacket::class

        override fun encode(output: Output, packet: ServerboundStatusRequestPacket) {
        }

        override fun decode(input: Input): ServerboundStatusRequestPacket = ServerboundStatusRequestPacket
    }
}