package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundPlayerAbilitiesPacket(
    val flags: Byte
) : MinecraftPacket {
    object Codec : PacketCodec<ServerboundPlayerAbilitiesPacket> {
        override val packetType: KClass<ServerboundPlayerAbilitiesPacket>
            get() = ServerboundPlayerAbilitiesPacket::class

        override fun encode(output: Output, packet: ServerboundPlayerAbilitiesPacket) {
            output.writeByte(packet.flags)
        }

        override fun decode(input: Input): ServerboundPlayerAbilitiesPacket {
            val flags = input.readByte()
            return ServerboundPlayerAbilitiesPacket(flags)
        }
    }
}