package com.karbonpowered.protocol.packet.serverbound.login

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readString
import com.karbonpowered.protocol.writeString
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundLoginStartPacket(
    val username: String
) : MinecraftPacket {
    companion object : com.karbonpowered.network.MessageCodec<ServerboundLoginStartPacket> {
        override val messageType: KClass<ServerboundLoginStartPacket>
            get() = ServerboundLoginStartPacket::class

        override fun decode(input: Input): ServerboundLoginStartPacket {
            val username = input.readString(16)
            return ServerboundLoginStartPacket(username)
        }

        override fun encode(output: Output, data: ServerboundLoginStartPacket) {
            output.writeString(data.username)
        }
    }
}