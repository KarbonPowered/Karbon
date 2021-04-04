package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readBoolean
import com.karbonpowered.protocol.writeBoolean
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
    companion object : MessageCodec<ServerboundPlayerPositionRotationPacket> {
        override val messageType: KClass<ServerboundPlayerPositionRotationPacket>
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

        override fun encode(output: Output, data: ServerboundPlayerPositionRotationPacket) {
            output.writeDouble(data.x)
            output.writeDouble(data.y)
            output.writeDouble(data.z)
            output.writeFloat(data.yaw)
            output.writeFloat(data.pitch)
            output.writeBoolean(data.onGround)
        }
    }
}