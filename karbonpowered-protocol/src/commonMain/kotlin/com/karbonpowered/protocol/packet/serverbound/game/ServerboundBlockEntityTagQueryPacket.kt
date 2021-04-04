package com.karbonpowered.protocol.packet.serverbound.game

import com.karbonpowered.math.unpackLongX
import com.karbonpowered.math.unpackLongY
import com.karbonpowered.math.unpackLongZ
import com.karbonpowered.math.vector.IntVector3
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readVarInt
import com.karbonpowered.protocol.writeBlockPosition
import com.karbonpowered.protocol.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ServerboundBlockEntityTagQueryPacket(
    val transactionId: Int,
    val x: Int,
    val y: Int,
    val z: Int
) : MinecraftPacket {
    companion object : MessageCodec<ServerboundBlockEntityTagQueryPacket> {
        override val messageType = ServerboundBlockEntityTagQueryPacket::class

        override fun decode(input: Input): ServerboundBlockEntityTagQueryPacket {
            val transactionId = input.readVarInt()
            val packedBlockPos = input.readLong()
            val x = unpackLongX(packedBlockPos)
            val y = unpackLongY(packedBlockPos)
            val z = unpackLongZ(packedBlockPos)
            return ServerboundBlockEntityTagQueryPacket(transactionId, x, y, z)
        }

        override fun encode(output: Output, data: ServerboundBlockEntityTagQueryPacket) {
            output.writeVarInt(data.transactionId)
            output.writeBlockPosition(data.x, data.y, data.z)
        }
    }
}
