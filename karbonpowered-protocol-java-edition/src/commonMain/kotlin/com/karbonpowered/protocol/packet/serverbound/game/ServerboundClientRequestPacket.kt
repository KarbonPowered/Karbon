package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.protocol.MagicValues
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundClientRequestPacket(
    val request: ClientRequest
) : MinecraftPacket {
    enum class ClientRequest {
        RESPAWN, STATS
    }

    object Codec : PacketCodec<ServerboundClientRequestPacket> {
        override val packetType: KClass<ServerboundClientRequestPacket>
            get() = ServerboundClientRequestPacket::class

        override fun encode(output: Output, packet: ServerboundClientRequestPacket) {
            output.writeVarInt(MagicValues.value(packet.request))
        }

        override fun decode(input: Input): ServerboundClientRequestPacket {
            val request = MagicValues.key<ClientRequest>(input.readVarInt())
            return ServerboundClientRequestPacket(request)
        }
    }
}