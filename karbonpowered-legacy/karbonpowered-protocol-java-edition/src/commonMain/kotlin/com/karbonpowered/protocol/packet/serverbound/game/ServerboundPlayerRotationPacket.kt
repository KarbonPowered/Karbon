package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readBoolean
import com.karbonpowered.server.writeBoolean
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundPlayerRotationPacket(
    val yaw: Float = 0f,
    val pitch: Float = 0f,
    val onGround: Boolean = true
) : MinecraftPacket {
    object Codec : PacketCodec<ServerboundPlayerRotationPacket> {
        override val packetType: KClass<ServerboundPlayerRotationPacket>
            get() = ServerboundPlayerRotationPacket::class

        override fun decode(input: Input): ServerboundPlayerRotationPacket {

            val yaw = input.readFloat()
            val pitch = input.readFloat()
            val onGround = input.readBoolean()
            return ServerboundPlayerRotationPacket(yaw, pitch, onGround)
        }

        override fun encode(output: Output, packet: ServerboundPlayerRotationPacket) {
            output.writeFloat(packet.yaw)
            output.writeFloat(packet.pitch)
            output.writeBoolean(packet.onGround)
        }
    }
}