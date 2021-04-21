package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readBoolean
import com.karbonpowered.protocol.writeBoolean
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundPlayerOnGroundPacket(
    val onGround: Boolean = true
) : MinecraftPacket {
    companion object : MessageCodec<ServerboundPlayerOnGroundPacket> {
        override val messageType: KClass<ServerboundPlayerOnGroundPacket>
            get() = ServerboundPlayerOnGroundPacket::class

        override fun decode(input: Input): ServerboundPlayerOnGroundPacket {
            val onGround = input.readBoolean()
            return ServerboundPlayerOnGroundPacket(onGround)
        }

        override fun encode(output: Output, data: ServerboundPlayerOnGroundPacket) {
            output.writeBoolean(data.onGround)
        }
    }
}