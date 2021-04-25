package com.karbonpowered.protocol.packet.clientbound.game

//TODO
//
//import com.karbonpowered.api.entity.living.player.GameMode
//import com.karbonpowered.api.entity.living.player.GameModes
//import com.karbonpowered.io.Codec
//import com.karbonpowered.server.packet.PacketCodec
//import com.karbonpowered.profile.GameProfile
//import com.karbonpowered.protocol.*
//import com.karbonpowered.protocol.util.writeUUID
//import com.karbonpowered.server.readVarInt
//import com.karbonpowered.server.writeString
//import com.karbonpowered.text.LiteralText
//import com.karbonpowered.text.Text
//import io.ktor.utils.io.core.*
//import kotlin.reflect.KClass
//
//data class ClientboundGamePlayerListPacket(
//    val action: PlayerListAction,
//    val entries: List<PlayerListEntry>
//) : MinecraftPacket {
//
//    companion object : PacketCodec<ClientboundGamePlayerListPacket> {
//        override val packetType: KClass<ClientboundGamePlayerListPacket>
//            get() = ClientboundGamePlayerListPacket::class
//
//        override fun decode(input: Input): ClientboundGamePlayerListPacket {
//            val action = input.readEnum<PlayerListAction>()
//
//            val entriesSize = input.readVarInt()
//            val entries = mutableListOf<PlayerListEntry>()
//            for (i in 0 until entriesSize) {
//                entries.add(action.decode(input))
//            }
//
//            return ClientboundGamePlayerListPacket(action, entries)
//        }
//
//        override fun encode(output: Output, data: ClientboundGamePlayerListPacket) {
//            output.writeEnum(data.action)
//            data
//            output.writeCollection(data.entries, data.action::encode)
//        }
//    }
//}
//
//enum class PlayerListAction : Codec<PlayerListEntry> {
//    ADD_PLAYER {
//        override fun encode(output: Output, data: PlayerListEntry) {
//            output.writeUUID(data.profile.uniqueId)
//            output.writeString(data.profile.name ?: "")
//            output.writeCollection(data.profile.properties) { out, property ->
//                out.writeString(property.name)
//                out.writeString(property.value)
//                if (property.signature != null) {
//                    out.writeBoolean(true)
//                    out.writeString(property.signature ?: "")
//                } else {
//                    out.writeBoolean(false)
//                }
//            }
//            output.writeVarInt(MagicValues.value<Byte>(data.gameMode ?: GameModes.SURVIVAL).toInt())
//            output.writeVarInt(data.latency)
//            output.writeOptionalText(data.displayName)
//        }
//
//        override fun decode(input: Input): PlayerListEntry {
//            val uuid = input.readUUID()
//            val username = input.readString(16)
//            val properties = mutableListOf<ProfileProperty>()
//            val propertiesSize = input.readVarInt()
//            for (i in 0 until propertiesSize) {
//                val name = input.readString()
//                val value = input.readString()
//                val signature = if (input.readBoolean()) {
//                    input.readString()
//                } else null
//                properties.add(ProfileProperty(name, value, signature))
//            }
//
//            val gameMode = MagicValues.key<GameMode>(input.readVarInt().toByte())
//            val latency = input.readInt()
//            val displayName = input.readOptionalText()
//            return PlayerListEntry(
//                GameProfile(uuid, username, properties),
//                latency,
//                gameMode,
//                displayName
//            )
//        }
//    },
//    UPDATE_GAME_MODE {
//        override fun encode(output: Output, data: PlayerListEntry) {
//            output.writeUUID(data.profile.uniqueId)
//            output.writeVarInt(MagicValues.value<Byte>(data.gameMode ?: GameModes.SURVIVAL).toInt())
//        }
//
//        override fun decode(input: Input): PlayerListEntry {
//            val profile = GameProfile(input.readUUID())
//            val gameMode = MagicValues.key<GameMode>(input.readVarInt().toByte())
//            return PlayerListEntry(profile, 0, gameMode, null)
//        }
//    },
//    UPDATE_LATENCY {
//        override fun encode(output: Output, data: PlayerListEntry) {
//            output.writeUUID(data.profile.uniqueId)
//            output.writeVarInt(data.latency)
//        }
//
//        override fun decode(input: Input): PlayerListEntry {
//            val profile = GameProfile(input.readUUID())
//            val latency = input.readInt()
//            return PlayerListEntry(profile, latency, null, null)
//        }
//    },
//    UPDATE_DISPLAY_NAME {
//        override fun encode(output: Output, data: PlayerListEntry) {
//            output.writeUUID(data.profile.uniqueId)
//            output.writeString(data.displayName.toString())
//        }
//
//        override fun decode(input: Input): PlayerListEntry {
//            val profile = GameProfile(input.readUUID())
//            val displayName = LiteralText(input.readString())
//            return PlayerListEntry(profile, 0, null, displayName)
//        }
//    },
//    REMOVE_PLAYER {
//        override fun encode(output: Output, data: PlayerListEntry) {
//            output.writeUUID(data.profile.uniqueId)
//        }
//
//        override fun decode(input: Input): PlayerListEntry {
//            val profile = GameProfile(input.readUUID())
//            return PlayerListEntry(profile, 0, null, null)
//        }
//    };
//
//    fun Output.writeOptionalText(text: Text?) {
//        if (text != null) {
//            writeBoolean(true)
//            writeString(text.toString())
//        } else {
//            writeBoolean(false)
//        }
//    }
//
//    fun Input.readOptionalText() = if (readBoolean()) LiteralText(readString()) else null
//}
//
//data class PlayerListEntry(
//    val profile: GameProfile,
//    val latency: Int,
//    val gameMode: GameMode?,
//    val displayName: Text?
//)