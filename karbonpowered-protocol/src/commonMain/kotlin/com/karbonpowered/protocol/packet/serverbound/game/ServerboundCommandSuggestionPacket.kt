package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.protocol.*
import com.karbonpowered.server.readString
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeString
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.core.Output

data class ServerboundCommandSuggestionPacket(
    val id: Int,
    val command: String
) : MinecraftPacket {
    companion object : PacketCodec<ServerboundCommandSuggestionPacket> {
        override val packetType = ServerboundCommandSuggestionPacket::class

        override fun decode(input: Input): ServerboundCommandSuggestionPacket {
            val id = input.readVarInt()
            val command = input.readString(32500)
            return ServerboundCommandSuggestionPacket(id, command)
        }

        override fun encode(output: Output, data: ServerboundCommandSuggestionPacket) {
            output.writeVarInt(data.id)
            output.writeString(data.command)
        }

    }
}