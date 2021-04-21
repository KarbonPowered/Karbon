package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.api.Identifier
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.*
import io.ktor.utils.io.core.*

data class ServerboundAdvancementTabPacket(
    val action: Action,
    val tabToOpen: Identifier?
) : MinecraftPacket {
    companion object : MessageCodec<ServerboundAdvancementTabPacket> {
        override val messageType = ServerboundAdvancementTabPacket::class

        override fun decode(input: Input): ServerboundAdvancementTabPacket {
            val action = input.readEnum<Action>()
            val tabToOpen = if (action == Action.TAB_OPENED) {
                Identifier(input.readString())
            } else null
            return ServerboundAdvancementTabPacket(action, tabToOpen)
        }

        override fun encode(output: Output, data: ServerboundAdvancementTabPacket) {
            output.writeEnum(data.action)
            output.writeString(data.tabToOpen?.toString() ?: "")
        }
    }

    enum class Action {
        TAB_OPENED,
        CLOSED_SCREEN
    }
}