package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.api.world.difficulty.Difficulty
import com.karbonpowered.protocol.MagicValues
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.core.*

data class ServerboundChangeDifficultyPacket(
    val difficulty: Difficulty
) : MinecraftPacket {
    companion object : PacketCodec<ServerboundChangeDifficultyPacket> {
        override val packetType = ServerboundChangeDifficultyPacket::class

        override fun decode(input: Input): ServerboundChangeDifficultyPacket {
            val difficulty = MagicValues.key<Difficulty>(input.readVarInt())
            return ServerboundChangeDifficultyPacket(difficulty)
        }

        override fun encode(output: Output, packet: ServerboundChangeDifficultyPacket) {
            output.writeVarInt(MagicValues.value(packet.difficulty))
        }
    }
}