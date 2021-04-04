package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.api.entity.living.player.GameMode
import com.karbonpowered.api.profile.GameProfile
import com.karbonpowered.minecraft.text.Text
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.writeString
import com.karbonpowered.protocol.writeUUID
import com.karbonpowered.protocol.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundPlayerListEntryPacket(
    val action: PlayerListEntryAction,
    val entries: List<PlayerListEntry>
) : MinecraftPacket {

    enum class PlayerListEntryAction(val id: Int) {
        ADD_PLAYER(0),
        UPDATE_GAMEMODE(1),
        UPDATE_LATENCY(2),
        UPDATE_DISPLAY_NAME(3),
        REMOVE_PLAYER(4);

        companion object {
            operator fun get(id: Int): PlayerListEntryAction = when(id) {
                ADD_PLAYER.id -> ADD_PLAYER
                UPDATE_GAMEMODE.id -> UPDATE_GAMEMODE
                UPDATE_LATENCY.id -> UPDATE_LATENCY
                UPDATE_DISPLAY_NAME.id -> UPDATE_DISPLAY_NAME
                REMOVE_PLAYER.id -> REMOVE_PLAYER
                else -> throw IllegalArgumentException("Unknown id: $id")
            }
        }
    }

    data class PlayerListEntry(
        val profile: GameProfile,
        val gameMode: GameMode,
        val ping: Int,
        val displayName: Text
    )

    companion object : MessageCodec<ClientboundPlayerListEntryPacket> {
        override val messageType: KClass<ClientboundPlayerListEntryPacket>
            get() = ClientboundPlayerListEntryPacket::class

        override fun decode(input: Input): ClientboundPlayerListEntryPacket {
            TODO("Not yet implemented")
        }

        override fun encode(output: Output, data: ClientboundPlayerListEntryPacket) {
            output.writeVarInt(data.action.id)
            output.writeVarInt(data.entries.size)
            data.entries.forEach { entry ->
                output.writeUUID(entry.profile.uniqueId)
                when(data.action) {
                    PlayerListEntryAction.ADD_PLAYER -> {
                        output.writeString(entry.profile.name ?: "")

                    }
                    PlayerListEntryAction.UPDATE_GAMEMODE -> TODO()
                    PlayerListEntryAction.UPDATE_LATENCY -> TODO()
                    PlayerListEntryAction.UPDATE_DISPLAY_NAME -> TODO()
                    PlayerListEntryAction.REMOVE_PLAYER -> TODO()
                }
            }
        }

    }
}