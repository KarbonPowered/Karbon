package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.api.entity.living.player.GameMode
import com.karbonpowered.nbt.NBT
import com.karbonpowered.protocol.MagicValues
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.util.readNBT
import com.karbonpowered.protocol.util.writeNBT
import com.karbonpowered.server.*
import com.karbonpowered.server.packet.PacketCodec
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundGameJoinPacket(
    val entityId: Int,
    val isHardcore: Boolean,
    val gameMode: GameMode,
    val previousGameMode: GameMode,
    val worlds: List<String>,
    val dimensionCodec: NBT,
    val dimension: NBT,
    val world: String,
    val hashedSeed: Long,
    val maxPlayers: Int,
    val viewDistance: Int,
    val reducedDebugInfo: Boolean,
    val enableRespawnScreen: Boolean,
    val isDebug: Boolean,
    val isFlat: Boolean
) : MinecraftPacket {
    object Codec : PacketCodec<ClientboundGameJoinPacket> {
        override val packetType: KClass<ClientboundGameJoinPacket> = ClientboundGameJoinPacket::class

        override fun encode(output: Output, packet: ClientboundGameJoinPacket) {
            output.writeInt(packet.entityId)
            output.writeBoolean(packet.isHardcore)
            output.writeByte(MagicValues.value(packet.gameMode))
            output.writeByte(MagicValues.value(packet.previousGameMode))
            output.writeVarInt(packet.worlds.size)
            packet.worlds.forEach {
                output.writeString(it)
            }
            output.writeNBT(packet.dimensionCodec)
            output.writeNBT(packet.dimension)
            output.writeString(packet.world)
            output.writeLong(packet.hashedSeed)
            output.writeVarInt(packet.maxPlayers)
            output.writeVarInt(packet.viewDistance)
            output.writeBoolean(packet.reducedDebugInfo)
            output.writeBoolean(packet.enableRespawnScreen)
            output.writeBoolean(packet.isDebug)
            output.writeBoolean(packet.isFlat)
        }

        override fun decode(input: Input): ClientboundGameJoinPacket {
            val entityId = input.readInt()
            val isHardcore = input.readBoolean()
            val gameMode = MagicValues.key<GameMode>(input.readByte())
            val previousGameMode = MagicValues.key<GameMode>(input.readByte())
            val worlds = List(input.readVarInt()) {
                input.readString()
            }
            val dimensionCodec = requireNotNull(input.readNBT())
            val dimension = requireNotNull(input.readNBT())
            val world = input.readString()
            val hashedSeed = input.readLong()
            val maxPlayers = input.readVarInt()
            val viewDistance = input.readVarInt()
            val reducedDebugInfo = input.readBoolean()
            val enableRespawnScreen = input.readBoolean()
            val isDebug = input.readBoolean()
            val isFlat = input.readBoolean()
            return ClientboundGameJoinPacket(
                entityId, isHardcore, gameMode, previousGameMode, worlds, dimensionCodec, dimension, world,
                hashedSeed, maxPlayers, viewDistance, reducedDebugInfo, enableRespawnScreen, isDebug, isFlat
            )
        }
    }
}