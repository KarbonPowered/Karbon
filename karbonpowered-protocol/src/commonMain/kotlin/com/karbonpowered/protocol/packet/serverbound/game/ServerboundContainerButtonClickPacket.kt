package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.protocol.MinecraftPacket
import io.ktor.utils.io.core.*

data class ServerboundContainerButtonClickPacket(
    val containerId: Int,
    val buttonId: Int
) : MinecraftPacket {
    companion object : PacketCodec<ServerboundContainerButtonClickPacket> {
        override val packetType = ServerboundContainerButtonClickPacket::class

        override fun decode(input: Input): ServerboundContainerButtonClickPacket {
            val containerId = input.readByte().toInt()
            val buttonId = input.readByte().toInt()
            return ServerboundContainerButtonClickPacket(containerId, buttonId)
        }

        override fun encode(output: Output, data: ServerboundContainerButtonClickPacket) {
            output.writeByte(data.containerId.toByte())
            output.writeByte(data.buttonId.toByte())
        }
    }
}