package com.karbonpowered.protocol.packet.serverbound.status

import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.protocol.MinecraftPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

class ServerboundStatusRequestPacket : MinecraftPacket {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        return true
    }

    override fun hashCode(): Int = this::class.hashCode()

    override fun toString(): String = "ServerboundStatusRequest()"

    companion object : PacketCodec<ServerboundStatusRequestPacket> {
        override val packetType: KClass<ServerboundStatusRequestPacket>
            get() = ServerboundStatusRequestPacket::class

        override fun decode(input: Input): ServerboundStatusRequestPacket = ServerboundStatusRequestPacket()

        override fun encode(output: Output, message: ServerboundStatusRequestPacket) {}
    }
}