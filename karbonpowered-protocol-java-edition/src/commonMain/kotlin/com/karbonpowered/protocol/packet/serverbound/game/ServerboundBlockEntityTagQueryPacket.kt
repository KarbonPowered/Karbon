package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.math.unpackLongX
import com.karbonpowered.math.unpackLongY
import com.karbonpowered.math.unpackLongZ
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.util.writeBlockPosition
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.core.*

data class ServerboundBlockEntityTagQueryPacket(
    val transactionId: Int,
    val x: Int,
    val y: Int,
    val z: Int
) : MinecraftPacket {
    object Codec : PacketCodec<ServerboundBlockEntityTagQueryPacket> {
        override val packetType = ServerboundBlockEntityTagQueryPacket::class

        override fun decode(input: Input): ServerboundBlockEntityTagQueryPacket {
            val transactionId = input.readVarInt()
            val packedBlockPos = input.readLong()
            val x = unpackLongX(packedBlockPos)
            val y = unpackLongY(packedBlockPos)
            val z = unpackLongZ(packedBlockPos)
            return ServerboundBlockEntityTagQueryPacket(transactionId, x, y, z)
        }

        override fun encode(output: Output, packet: ServerboundBlockEntityTagQueryPacket) {
            output.writeVarInt(packet.transactionId)
            output.writeBlockPosition(packet.x, packet.y, packet.z)
        }
    }
}
