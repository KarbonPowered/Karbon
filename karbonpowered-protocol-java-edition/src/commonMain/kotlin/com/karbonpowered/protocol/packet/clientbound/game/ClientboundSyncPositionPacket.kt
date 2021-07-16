package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundSyncPositionPacket(
    val chunkX: Int,
    val chunkZ: Int
) : MinecraftPacket {
    object Codec : PacketCodec<ClientboundSyncPositionPacket> {
        override val packetType: KClass<ClientboundSyncPositionPacket> = ClientboundSyncPositionPacket::class

        override fun encode(output: Output, packet: ClientboundSyncPositionPacket) {
            output.writeVarInt(packet.chunkX)
            output.writeVarInt(packet.chunkZ)
        }

        override fun decode(input: Input): ClientboundSyncPositionPacket {
            val chunkX = input.readVarInt()
            val chunkZ = input.readVarInt()
            return ClientboundSyncPositionPacket(chunkX, chunkZ)
        }
    }
}