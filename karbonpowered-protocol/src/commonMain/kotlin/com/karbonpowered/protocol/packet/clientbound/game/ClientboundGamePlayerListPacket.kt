package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.api.entity.living.player.GameMode
import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.profile.GameProfile
import com.karbonpowered.api.profile.property.ProfileProperty
import com.karbonpowered.io.Codec
import com.karbonpowered.minecraft.text.LiteralText
import com.karbonpowered.minecraft.text.Text
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.*
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundGamePlayerListPacket(
    val action: Action,
    val players: List<GameProfile>
) : MinecraftPacket {
    val entries = mutableListOf<PlayerListEntry>()

    init {
        players.forEach {
            entries.add(
                PlayerListEntry(
                    0, // TODO: Player ping

                )
            )
        }
    }

    companion object : MessageCodec<ClientboundGamePlayerListPacket> {
        override val messageType: KClass<ClientboundGamePlayerListPacket>
            get() = ClientboundGamePlayerListPacket::class

        override fun decode(input: Input): ClientboundGamePlayerListPacket {

        }

        override fun encode(output: Output, data: ClientboundGamePlayerListPacket) {
            TODO("Not yet implemented")
        }
    }
}

enum class Action : Codec<PlayerListEntry> {
    ADD_PLAYER {
        override fun encode(output: Output, data: PlayerListEntry) {
            output.writeUUID(data.uniqueId)
            output.writeString(data.username)
            output.writeCollection(data.profileProperties) { out, property ->
                out.writeString(property.name)
                out.writeString(property.value)
                if (property.signature != null) {
                    out.writeBoolean(true)
                    out.writeString(property.signature ?: "")
                } else {
                    out.writeBoolean(false)
                }
            }
            output.writeVarInt(MagicValues.value<Byte>(data.gameMode).toInt())
            output.writeVarInt(data.latency)
            output.writeOptionalText(data.displayName)
        }

        override fun decode(input: Input): PlayerListEntry {
            val uuid = input.readUUID()
            val username = input.readString(16)
            val properties = mutableListOf<ProfileProperty>()
            val propertiesSize = input.readVarInt()
            for (i in 0 until propertiesSize) {
                val name = input.readString()
                val value = input.readString()
                val signature = if (input.readBoolean()) {
                    input.readString()
                } else null
                properties.add(object : ProfileProperty {
                    override val name = name
                    override val value = value
                    override val signature = signature
                })
            }

            val gameMode = MagicValues.key<GameMode>(input.readVarInt().toByte())
            val latency = input.readInt()
            val text = input.readOptionalText()

        }
    },
    UPDATE_GAME_MODE {
        override fun encode(output: Output, data: PlayerListEntry) {
            TODO("Not yet implemented")
        }

        override fun decode(input: Input): PlayerListEntry {
            TODO("Not yet implemented")
        }
    },
    UPDATE_LATENCY,
    UPDATE_DISPLAY_NAME,
    REMOVE_PLAYER;

    fun Output.writeOptionalText(text: Text?) {
        if (text != null) {
            writeBoolean(true)
            writeString(text.toString())
        } else {
            writeBoolean(false)
        }
    }

    fun Input.readOptionalText() = if (readBoolean()) LiteralText(readString()) else null
}

data class PlayerListEntry(
    val profile: GameProfile,
    val latency: Int,
    val gameMode: GameMode,
    val profileProperties: List<ProfileProperty>,
    val displayName: Text?
)