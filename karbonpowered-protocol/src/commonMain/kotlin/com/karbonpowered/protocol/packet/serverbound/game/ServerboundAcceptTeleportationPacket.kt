package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readVarInt
import io.ktor.utils.io.core.*

data class ServerboundAcceptTeleportationPacket(
    val id: Int
) : MinecraftPacket {
    companion object : MessageCodec<ServerboundAcceptTeleportationPacket> {
        override val messageType = ServerboundAcceptTeleportationPacket::class

        override fun decode(input: Input) = ServerboundAcceptTeleportationPacket(input.readVarInt())

        override fun encode(output: Output, data: ServerboundAcceptTeleportationPacket) = output.writeInt(data.id)
    }
}