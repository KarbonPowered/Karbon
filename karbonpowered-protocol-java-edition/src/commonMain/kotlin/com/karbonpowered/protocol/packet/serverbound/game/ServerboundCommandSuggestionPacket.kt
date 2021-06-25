package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readString
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeString
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.core.*

data class ServerboundCommandSuggestionPacket(
    val id: Int,
    val command: String
) : MinecraftPacket {
    object Codec : PacketCodec<ServerboundCommandSuggestionPacket> {
        override val packetType = ServerboundCommandSuggestionPacket::class

        override fun decode(input: Input): ServerboundCommandSuggestionPacket {
            val id = input.readVarInt()
            val command = input.readString(32500)
            return ServerboundCommandSuggestionPacket(id, command)
        }

        override fun encode(output: Output, packet: ServerboundCommandSuggestionPacket) {
            output.writeVarInt(packet.id)
            output.writeString(packet.command)
        }

    }
}