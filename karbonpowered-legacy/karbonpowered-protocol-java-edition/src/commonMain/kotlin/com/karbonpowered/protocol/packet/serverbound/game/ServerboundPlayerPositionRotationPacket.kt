package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readBoolean
import com.karbonpowered.server.writeBoolean
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundPlayerPositionRotationPacket(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
    val yaw: Float = 0f,
    val pitch: Float = 0f,
    val onGround: Boolean = true
) : MinecraftPacket {
    object Codec : PacketCodec<ServerboundPlayerPositionRotationPacket> {
        override val packetType: KClass<ServerboundPlayerPositionRotationPacket>
            get() = ServerboundPlayerPositionRotationPacket::class

        override fun decode(input: Input): ServerboundPlayerPositionRotationPacket {
            val x = input.readDouble()
            val y = input.readDouble()
            val z = input.readDouble()
            val yaw = input.readFloat()
            val pitch = input.readFloat()
            val onGround = input.readBoolean()
            return ServerboundPlayerPositionRotationPacket(x, y, z, yaw, pitch, onGround)
        }

        override fun encode(output: Output, packet: ServerboundPlayerPositionRotationPacket) {
            output.writeDouble(packet.x)
            output.writeDouble(packet.y)
            output.writeDouble(packet.z)
            output.writeFloat(packet.yaw)
            output.writeFloat(packet.pitch)
            output.writeBoolean(packet.onGround)
        }
    }
}