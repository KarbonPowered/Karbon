package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.minecraft.text.LiteralText
import com.karbonpowered.minecraft.text.Text
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.*
import io.ktor.utils.io.core.*

data class ClientboundSetScoreboardObjectivePacket(
    val name: String,
    val displayName: Text,
    val scoreboardRenderType: ScoreboardRenderType,
    val mode: Int
) : MinecraftPacket {
    companion object : MessageCodec<ClientboundSetScoreboardObjectivePacket> {
        override val messageType = ClientboundSetScoreboardObjectivePacket::class

        override fun decode(input: Input): ClientboundSetScoreboardObjectivePacket {
            val name = input.readString(16)
            val mode = input.readByte().toInt()
            val (displayName, scoreboardRenderType) = if (mode != 0 && mode != 2) {
                Pair(LiteralText(""), ScoreboardRenderType.INTEGER)
            } else Pair(LiteralText(input.readString()), input.readEnum())
            return ClientboundSetScoreboardObjectivePacket(name, displayName, scoreboardRenderType, mode)
        }

        override fun encode(output: Output, data: ClientboundSetScoreboardObjectivePacket) {
            output.writeString(data.name)
            output.writeByte(data.mode.toByte())
            if (data.mode == 0 || data.mode == 2) {
                output.writeText(data.displayName.toString())
                output.writeEnum(data.scoreboardRenderType)
            }
        }
    }
}

enum class ScoreboardRenderType { // TODO: do scoreboard objective and other classes
    INTEGER,
    HEARTS
}