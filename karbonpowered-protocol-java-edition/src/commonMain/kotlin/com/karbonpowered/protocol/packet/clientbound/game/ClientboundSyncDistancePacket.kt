package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundSyncDistancePacket(
    val distance: Int
) : MinecraftPacket {
    object Codec : PacketCodec<ClientboundSyncDistancePacket> {
        override val packetType: KClass<ClientboundSyncDistancePacket> = ClientboundSyncDistancePacket::class

        override fun encode(output: Output, packet: ClientboundSyncDistancePacket) {
            output.writeVarInt(packet.distance)
        }

        override fun decode(input: Input): ClientboundSyncDistancePacket {
            val distance = input.readVarInt()
            return ClientboundSyncDistancePacket(distance)
        }
    }
}