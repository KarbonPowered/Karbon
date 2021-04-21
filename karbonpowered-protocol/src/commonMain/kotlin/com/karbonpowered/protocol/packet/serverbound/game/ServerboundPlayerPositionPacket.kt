package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readBoolean
import com.karbonpowered.protocol.writeBoolean
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundPlayerPositionPacket(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
    val onGround: Boolean = true
) : MinecraftPacket {
    companion object : MessageCodec<ServerboundPlayerPositionPacket> {
        override val messageType: KClass<ServerboundPlayerPositionPacket>
            get() = ServerboundPlayerPositionPacket::class

        override fun decode(input: Input): ServerboundPlayerPositionPacket {
            val x = input.readDouble()
            val y = input.readDouble()
            val z = input.readDouble()
            val onGround = input.readBoolean()
            return ServerboundPlayerPositionPacket(x, y, z, onGround)
        }

        override fun encode(output: Output, data: ServerboundPlayerPositionPacket) {
            output.writeDouble(data.x)
            output.writeDouble(data.y)
            output.writeDouble(data.z)
            output.writeBoolean(data.onGround)
        }
    }
}