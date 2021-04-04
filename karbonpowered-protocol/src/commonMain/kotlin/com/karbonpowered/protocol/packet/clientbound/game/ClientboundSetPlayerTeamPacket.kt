package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.io.Codec
import com.karbonpowered.minecraft.text.LiteralText
import com.karbonpowered.minecraft.text.Text
import com.karbonpowered.minecraft.text.format.Formatting
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.*
import io.ktor.utils.io.core.*

data class ClientboundSetPlayerTeamPacket(
    val packetType: Int, // 1-4
    val teamName: String,
    val players: Collection<String>,
    val team: SerializableTeam?
) : MinecraftPacket {
    fun containsPlayers() = Companion.containsPlayers(packetType)

    fun containsTeamsInfo() = containsTeamsInfo(packetType)

    companion object : MessageCodec<ClientboundSetPlayerTeamPacket> {
        override val messageType = ClientboundSetPlayerTeamPacket::class

        fun containsPlayers(packetType: Int) = packetType == 0 || packetType == 3 || packetType == 4

        fun containsTeamsInfo(packetType: Int) = packetType == 0 || packetType == 2

        override fun decode(input: Input): ClientboundSetPlayerTeamPacket {
            val teamName = input.readString(16)
            val packetType = input.readByte().toInt()

            val team = if (containsTeamsInfo(packetType)) {
                SerializableTeam.decode(input)
            } else null

            val players = mutableListOf<String>()
            if (containsPlayers(packetType)) {
                val size = input.readVarInt()
                for (i in 0 until size) {
                    players.add(input.readString())
                }
            }

            return ClientboundSetPlayerTeamPacket(packetType, teamName, players, team)
        }

        override fun encode(output: Output, data: ClientboundSetPlayerTeamPacket) {
            output.writeString(data.teamName)
            output.writeByte(data.packetType.toByte())
            if (data.containsTeamsInfo() && data.team != null) {
                SerializableTeam.encode(output, data.team)
            }

            if (data.containsPlayers()) {
                output.writeCollection(data.players, Output::writeString)
            }
        }
    }
}

data class SerializableTeam(
    val displayName: Text,
    val prefix: Text,
    val suffix: Text,
    val nameTagVisibilityRule: String,
    val collisionRule: String,
    val color: Formatting,
    val friendlyFlags: Int
) {
    companion object : Codec<SerializableTeam> {
        override fun encode(output: Output, data: SerializableTeam) {
            output.writeString(data.displayName.toString())
            output.writeByte(data.friendlyFlags.toByte())
            output.writeString(data.nameTagVisibilityRule)
            output.writeString(data.collisionRule)
            output.writeEnum(data.color)
            output.writeString(data.prefix.toString())
            output.writeString(data.suffix.toString())
        }

        override fun decode(input: Input): SerializableTeam {
            val displayName = LiteralText(input.readString())
            val friendlyFlags = input.readByte()
            val nameTagVisibilityRule = input.readString(40)
            val collisionRule = input.readString(40)
            val color = input.readEnum<Formatting>()
            val prefix = LiteralText(input.readString())
            val suffix = LiteralText(input.readString())
            return SerializableTeam(displayName, prefix, suffix, nameTagVisibilityRule, collisionRule, color, friendlyFlags.toInt())
        }
    }
}