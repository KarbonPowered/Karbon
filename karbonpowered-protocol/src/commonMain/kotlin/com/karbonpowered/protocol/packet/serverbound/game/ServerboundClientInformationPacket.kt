package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.api.entity.living.HumanoidArm
import com.karbonpowered.api.entity.living.player.ChatVisibility
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.*
import io.ktor.utils.io.core.*

data class ServerboundClientInformationPacket(
    val language: String,
    val viewDistance: Int,
    val chatVisibility: ChatVisibility,
    val chatColors: Boolean,
    val modelCustomisation: Int,
    val mainHand: HumanoidArm,
    val textFilteringEnabled: Boolean
) : MinecraftPacket {
    companion object : MessageCodec<ServerboundClientInformationPacket> {
        override val messageType = ServerboundClientInformationPacket::class

        override fun decode(input: Input): ServerboundClientInformationPacket {
            val language = input.readUTF8Line() ?: "en-EN"
            val viewDistance = input.readByte().toInt()
            val chatVisibility = input.readEnum<ChatVisibility>()
            val chatColors = input.readBoolean()
            val modelCustomisation = input.readUByte().toInt()
            val mainHand = input.readEnum<HumanoidArm>()
            val textFilteringEnabled = input.readBoolean()
            return ServerboundClientInformationPacket(language, viewDistance, chatVisibility, chatColors, modelCustomisation, mainHand, textFilteringEnabled)
        }

        override fun encode(output: Output, data: ServerboundClientInformationPacket) {
            output.writeString(data.language)
            output.writeByte(data.viewDistance.toByte())
            output.writeEnum(data.chatVisibility)
            output.writeBoolean(data.chatColors)
            output.writeInt(data.modelCustomisation)
            output.writeEnum(data.mainHand)
            output.writeBoolean(data.textFilteringEnabled)
        }

    }
}