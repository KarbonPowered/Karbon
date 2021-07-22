package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.api.entity.living.player.ChatVisibility
import com.karbonpowered.api.entity.living.player.ClientSettings
import com.karbonpowered.api.entity.living.player.HandType
import com.karbonpowered.api.entity.living.player.SkinPart
import com.karbonpowered.core.entity.living.player.ClientSettingsImpl
import com.karbonpowered.core.entity.living.player.SkinParts
import com.karbonpowered.protocol.MagicValues
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.*
import com.karbonpowered.server.packet.PacketCodec
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundClientSettingsPacket(
    val clientSettings: ClientSettings
) : MinecraftPacket {
    object Codec : PacketCodec<ServerboundClientSettingsPacket> {
        private val SKIN_PARTS = arrayOf(
            SkinParts.CAPE,
            SkinParts.JACKET,
            SkinParts.LEFT_SLEEVE,
            SkinParts.RIGHT_SLEEVE,
            SkinParts.HAT,
            SkinParts.LEFT_PANTS_LEG,
            SkinParts.RIGHT_PANTS_LEG,
        )

        override val packetType: KClass<ServerboundClientSettingsPacket>
            get() = ServerboundClientSettingsPacket::class

        override fun encode(output: Output, packet: ServerboundClientSettingsPacket) {
            val clientSettings = packet.clientSettings
            output.writeString(clientSettings.locale)
            output.writeByte(clientSettings.viewDistance.toByte())
            output.writeVarInt(MagicValues.value(clientSettings.chatVisibility))
            output.writeBoolean(clientSettings.hasChatColors)
            output.writeByte(writeSkinParts(clientSettings.skinParts))
            output.writeVarInt(MagicValues.value(clientSettings.mainHand))
            output.writeBoolean(clientSettings.textFiltering)
        }

        override fun decode(input: Input): ServerboundClientSettingsPacket {
            val locale = input.readString()
            val viewDistance = input.readByte().toInt()
            val chatVisibility = MagicValues.key<ChatVisibility>(input.readVarInt())
            val hasChatColors = input.readBoolean()
            val skinParts = readSkinParts(input.readByte().toInt())
            val mainHand = MagicValues.key<HandType>(input.readVarInt())
            val textFiltering = input.readBoolean()
            val clientSettings = ClientSettingsImpl(
                locale,
                viewDistance,
                chatVisibility,
                hasChatColors,
                skinParts,
                mainHand,
                textFiltering
            )
            return ServerboundClientSettingsPacket(clientSettings)
        }

        private fun readSkinParts(flags: Int): Set<SkinPart> {
            val skinParts = mutableSetOf<SkinPart>()
            SKIN_PARTS.forEachIndexed { index, skinPart ->
                val bit = 1 shl index
                if ((flags and bit) == bit) {
                    skinParts.add(skinPart)
                }
            }
            return skinParts
        }

        private fun writeSkinParts(skinParts: Iterable<SkinPart>): Byte {
            var flags = 0
            skinParts.forEach { skinPart ->
                flags = flags or 1 shl SKIN_PARTS.indexOf(skinPart)
            }
            return flags.toByte()
        }
    }
}