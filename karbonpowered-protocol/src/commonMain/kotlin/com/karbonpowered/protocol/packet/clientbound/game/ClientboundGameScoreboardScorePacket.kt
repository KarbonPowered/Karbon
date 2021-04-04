package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.*
import io.ktor.utils.io.core.*

data class ClientboundGameScoreboardScorePacket(
    val playerName: String,
    val objectiveName: String?,
    val score: Int,
    val mode: ChangeMode
) : MinecraftPacket {
    enum class ChangeMode {
        CHANGE,
        REMOVE
    }

    init {
        require(mode != ChangeMode.REMOVE && objectiveName == null) { "Need an objective name" }
    }

    companion object : MessageCodec<ClientboundGameScoreboardScorePacket> {
        override val messageType = ClientboundGameScoreboardScorePacket::class

        override fun decode(input: Input): ClientboundGameScoreboardScorePacket {
            val player = input.readString(40)
            val mode = input.readEnum<ChangeMode>()
            val name = input.readString().let { if (it == "") it else null }
            val score = if (mode != ChangeMode.REMOVE) input.readVarInt() else 0
            return ClientboundGameScoreboardScorePacket(player, name, score, mode)
        }

        override fun encode(output: Output, data: ClientboundGameScoreboardScorePacket) {
            output.writeString(data.playerName)
            output.writeEnum(data.mode)
            output.writeString(data.objectiveName ?: "")
            if (data.mode != ChangeMode.REMOVE) {
                output.writeVarInt(data.score)
            }
        }

    }
}