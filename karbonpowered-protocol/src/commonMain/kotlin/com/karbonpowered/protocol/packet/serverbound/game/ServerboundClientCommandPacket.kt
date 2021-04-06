package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readEnum
import com.karbonpowered.protocol.writeEnum
import io.ktor.utils.io.core.*

data class ServerboundClientCommandPacket(
    val action: Action
) : MinecraftPacket {
    enum class Action {
        PERFORM_RESPAWN,
        REQUEST_STATS
    }

    companion object : MessageCodec<ServerboundClientCommandPacket> {
        override val messageType = ServerboundClientCommandPacket::class

        override fun encode(output: Output, data: ServerboundClientCommandPacket) = output.writeEnum(data.action)

        override fun decode(input: Input) = ServerboundClientCommandPacket(input.readEnum())
    }
}