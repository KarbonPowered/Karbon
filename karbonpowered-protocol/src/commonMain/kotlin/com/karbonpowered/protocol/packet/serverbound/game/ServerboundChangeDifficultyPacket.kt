package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import io.ktor.utils.io.core.*

data class ServerboundChangeDifficultyPacket(
    val difficulty: Difficulty
) : MinecraftPacket {
    companion object : MessageCodec<ServerboundChangeDifficultyPacket> {
        override val messageType = ServerboundChangeDifficultyPacket::class

        override fun decode(input: Input) = ServerboundChangeDifficultyPacket(Difficulty.byId(input.readByte().toInt()))

        override fun encode(output: Output, data: ServerboundChangeDifficultyPacket) =
            output.writeByte(data.difficulty.id.toByte())
    }
}