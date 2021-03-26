package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.api.entity.living.player.GameMode
import com.karbonpowered.nbt.NBT
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.*
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
    companion object : MessageCodec<ClientboundGameJoinPacket> {
        override val messageType: KClass<ClientboundGameJoinPacket> = ClientboundGameJoinPacket::class

        override suspend fun encode(output: Output, data: ClientboundGameJoinPacket) {
            output.writeInt(data.entityId)
            output.writeBoolean(data.isHardcore)
            output.writeByte(MagicValues.value(Byte::class, data.gameMode))
            output.writeByte(MagicValues.value(Byte::class, data.previousGameMode))
            output.writeVarInt(data.worlds.size)
            data.worlds.forEach {
                output.writeString(it)
            }
            output.writeNBT(data.dimensionCodec)
            output.writeNBT(data.dimension)
            output.writeString(data.world)
            output.writeLong(data.hashedSeed)
            output.writeVarInt(data.maxPlayers)
            output.writeVarInt(data.viewDistance)
            output.writeBoolean(data.reducedDebugInfo)
            output.writeBoolean(data.enableRespawnScreen)
            output.writeBoolean(data.isDebug)
            output.writeBoolean(data.isFlat)
        }

        override suspend fun decode(input: Input): ClientboundGameJoinPacket {
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