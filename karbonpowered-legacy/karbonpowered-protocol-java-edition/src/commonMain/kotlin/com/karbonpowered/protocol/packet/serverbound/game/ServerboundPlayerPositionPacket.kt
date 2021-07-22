package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readBoolean
import com.karbonpowered.server.writeBoolean
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundPlayerPositionPacket(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
    val onGround: Boolean = true
) : MinecraftPacket {
    object Codec : PacketCodec<ServerboundPlayerPositionPacket> {
        override val packetType: KClass<ServerboundPlayerPositionPacket>
            get() = ServerboundPlayerPositionPacket::class

        override fun decode(input: Input): ServerboundPlayerPositionPacket {
            val x = input.readDouble()
            val y = input.readDouble()
            val z = input.readDouble()
            val onGround = input.readBoolean()
            return ServerboundPlayerPositionPacket(x, y, z, onGround)
        }

        override fun encode(output: Output, packet: ServerboundPlayerPositionPacket) {
            output.writeDouble(packet.x)
            output.writeDouble(packet.y)
            output.writeDouble(packet.z)
            output.writeBoolean(packet.onGround)
        }
    }
}