package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import io.ktor.utils.io.core.*

data class ServerboundContainerButtonClickPacket(
    val containerId: Int,
    val buttonId: Int
) : MinecraftPacket {
    object Codec : PacketCodec<ServerboundContainerButtonClickPacket> {
        override val packetType = ServerboundContainerButtonClickPacket::class

        override fun decode(input: Input): ServerboundContainerButtonClickPacket {
            val containerId = input.readByte().toInt()
            val buttonId = input.readByte().toInt()
            return ServerboundContainerButtonClickPacket(containerId, buttonId)
        }

        override fun encode(output: Output, packet: ServerboundContainerButtonClickPacket) {
            output.writeByte(packet.containerId.toByte())
            output.writeByte(packet.buttonId.toByte())
        }
    }
}