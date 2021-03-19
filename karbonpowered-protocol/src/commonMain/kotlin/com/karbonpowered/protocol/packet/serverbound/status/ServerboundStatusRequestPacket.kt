package com.karbonpowered.protocol.packet.serverbound.status

import com.karbonpowered.network.Codec
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

    companion object : Codec<ServerboundStatusRequestPacket> {
        override val messageType: KClass<ServerboundStatusRequestPacket>
            get() = ServerboundStatusRequestPacket::class

        override suspend fun decode(input: Input): ServerboundStatusRequestPacket = ServerboundStatusRequestPacket()

        override suspend fun encode(output: Output, message: ServerboundStatusRequestPacket) {}
    }
}