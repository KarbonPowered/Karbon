package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.math.unpackLongX
import com.karbonpowered.math.unpackLongY
import com.karbonpowered.math.unpackLongZ
import com.karbonpowered.math.vector.IntVector3
import com.karbonpowered.math.vector.intVector3Of
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readVarInt
import com.karbonpowered.protocol.writeBlockPosition
import com.karbonpowered.protocol.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundGameBlockBreakingProgressPacket(
    val entityId: Int,
    val blockPos: IntVector3,
    val progress: Byte
) : MinecraftPacket {
    companion object : MessageCodec<ClientboundGameBlockBreakingProgressPacket> {
        override val messageType = ClientboundGameBlockBreakingProgressPacket::class

        override fun encode(output: Output, data: ClientboundGameBlockBreakingProgressPacket) {
            output.writeVarInt(data.entityId)
            output.writeBlockPosition(data.blockPos.x, data.blockPos.y, data.blockPos.z)
            output.writeByte(data.progress)
        }

        override fun decode(input: Input): ClientboundGameBlockBreakingProgressPacket {
            val entityId = input.readVarInt()
            val packedLocation = input.readLong()
            val location =
                intVector3Of(unpackLongX(packedLocation), unpackLongY(packedLocation), unpackLongZ(packedLocation))
            val progress = input.readByte()
            return ClientboundGameBlockBreakingProgressPacket(entityId, location, progress)
        }
    }
}