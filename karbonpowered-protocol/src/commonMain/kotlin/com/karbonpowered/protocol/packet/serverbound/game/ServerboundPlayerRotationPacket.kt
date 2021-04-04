package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readBoolean
import com.karbonpowered.protocol.writeBoolean
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundPlayerRotationPacket(
        val yaw: Float = 0f,
        val pitch: Float = 0f,
        val onGround: Boolean = true
) : MinecraftPacket {
    companion object : MessageCodec<ServerboundPlayerRotationPacket> {
        override val messageType: KClass<ServerboundPlayerRotationPacket>
            get() = ServerboundPlayerRotationPacket::class

        override fun decode(input: Input): ServerboundPlayerRotationPacket {

            val yaw = input.readFloat()
            val pitch = input.readFloat()
            val onGround = input.readBoolean()
            return ServerboundPlayerRotationPacket(yaw, pitch, onGround)
        }

        override fun encode(output: Output, data: ServerboundPlayerRotationPacket) {
            output.writeFloat(data.yaw)
            output.writeFloat(data.pitch)
            output.writeBoolean(data.onGround)
        }
    }
}