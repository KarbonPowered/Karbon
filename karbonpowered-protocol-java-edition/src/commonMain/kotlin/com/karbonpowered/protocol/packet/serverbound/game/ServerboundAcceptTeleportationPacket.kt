package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readVarInt
import io.ktor.utils.io.core.*

data class ServerboundAcceptTeleportationPacket(
    val id: Int
) : MinecraftPacket {
    companion object : PacketCodec<ServerboundAcceptTeleportationPacket> {
        override val packetType = ServerboundAcceptTeleportationPacket::class

        override fun decode(input: Input): ServerboundAcceptTeleportationPacket {
            val id = input.readVarInt()
            return ServerboundAcceptTeleportationPacket(id)
        }

        override fun encode(output: Output, packet: ServerboundAcceptTeleportationPacket) {
            output.writeInt(packet.id)
        }
    }
}