package com.karbonpowered.protocol.packet.serverbound.status

import com.karbonpowered.network.Codec
import com.karbonpowered.protocol.MinecraftPacket
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

class ServerboundStatusRequest : MinecraftPacket {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        return true
    }

    override fun hashCode(): Int = this::class.hashCode()

    override fun toString(): String = "ServerboundStatusRequest()"

    companion object : Codec<ServerboundStatusRequest> {
        override val messageType: KClass<ServerboundStatusRequest>
            get() = ServerboundStatusRequest::class

        override suspend fun decode(input: Input): ServerboundStatusRequest = ServerboundStatusRequest()

        override suspend fun encode(output: Output, message: ServerboundStatusRequest) {}
    }
}