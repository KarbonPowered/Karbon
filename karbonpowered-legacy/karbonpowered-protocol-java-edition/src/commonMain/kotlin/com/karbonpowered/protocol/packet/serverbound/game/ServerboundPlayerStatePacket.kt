package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.protocol.MagicValues
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundPlayerStatePacket(
    val entityId: Int,
    val state: PlayerState,
    val jumpBoost: Int = 0
) : MinecraftPacket {
    enum class PlayerState {
        START_SNEAKING,
        STOP_SNEAKING,
        LEAVE_BED,
        START_SPRINTING,
        STOP_SPRINTING,
        START_HORSE_JUMP,
        STOP_HORSE_JUMP,
        OPEN_HORSE_INVENTORY,
        START_ELYTRA_FLYING
    }

    object Codec : PacketCodec<ServerboundPlayerStatePacket> {
        override val packetType: KClass<ServerboundPlayerStatePacket>
            get() = ServerboundPlayerStatePacket::class

        override fun encode(output: Output, packet: ServerboundPlayerStatePacket) {
            output.writeVarInt(packet.entityId)
            output.writeVarInt(MagicValues.value(packet.state))
            output.writeVarInt(packet.jumpBoost)
        }

        override fun decode(input: Input): ServerboundPlayerStatePacket {
            val entityId = input.readVarInt()
            val state = MagicValues.key<PlayerState>(input.readVarInt())
            val jumpBoost = input.readVarInt()
            return ServerboundPlayerStatePacket(entityId, state, jumpBoost)
        }
    }
}