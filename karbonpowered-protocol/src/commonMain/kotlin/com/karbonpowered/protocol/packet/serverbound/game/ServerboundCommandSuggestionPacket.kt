package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readVarInt
import com.karbonpowered.protocol.writeString
import com.karbonpowered.protocol.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundCommandSuggestionPacket(
    val id: Int,
    val command: String
) : MinecraftPacket {
    companion object : MessageCodec<ServerboundCommandSuggestionPacket> {
        override val messageType = ServerboundCommandSuggestionPacket::class

        override fun decode(input: Input): ServerboundCommandSuggestionPacket {
            val id = input.readVarInt()
            val command = input.readUTF8Line(limit = 32500) ?: ""
            return ServerboundCommandSuggestionPacket(id, command)
        }

        override fun encode(output: Output, data: ServerboundCommandSuggestionPacket) {
            output.writeVarInt(data.id)
            output.writeString(data.command)
        }

    }
}