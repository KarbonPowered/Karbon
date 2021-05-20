package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readBoolean
import com.karbonpowered.server.writeBoolean
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundPlayerOnGroundPacket(
    val onGround: Boolean = true
) : MinecraftPacket {
    companion object : PacketCodec<ServerboundPlayerOnGroundPacket> {
        override val packetType: KClass<ServerboundPlayerOnGroundPacket>
            get() = ServerboundPlayerOnGroundPacket::class

        override fun decode(input: Input): ServerboundPlayerOnGroundPacket {
            val onGround = input.readBoolean()
            return ServerboundPlayerOnGroundPacket(onGround)
        }

        override fun encode(output: Output, packet: ServerboundPlayerOnGroundPacket) {
            output.writeBoolean(packet.onGround)
        }
    }
}