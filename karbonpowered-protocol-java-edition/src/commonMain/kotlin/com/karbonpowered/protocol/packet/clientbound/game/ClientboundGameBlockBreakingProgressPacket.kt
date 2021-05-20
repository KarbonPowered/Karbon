package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.math.unpackLongX
import com.karbonpowered.math.unpackLongY
import com.karbonpowered.math.unpackLongZ
import com.karbonpowered.math.vector.IntVector3
import com.karbonpowered.math.vector.intVector3Of
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.util.writeBlockPosition
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.core.*

data class ClientboundGameBlockBreakingProgressPacket(
    val entityId: Int,
    val x: Int,
    val y: Int,
    val z: Int,
    val progress: Int
) : MinecraftPacket {
    constructor(entityId: Int, blockPos: IntVector3, progress: Int) : this(
        entityId,
        blockPos.x,
        blockPos.y,
        blockPos.z,
        progress
    )

    companion object : PacketCodec<ClientboundGameBlockBreakingProgressPacket> {
        override val packetType = ClientboundGameBlockBreakingProgressPacket::class

        override fun encode(output: Output, packet: ClientboundGameBlockBreakingProgressPacket) {
            output.writeVarInt(packet.entityId)
            output.writeBlockPosition(packet.x, packet.y, packet.z)
            output.writeByte(packet.progress.toByte())
        }

        override fun decode(input: Input): ClientboundGameBlockBreakingProgressPacket {
            val entityId = input.readVarInt()
            val packedLocation = input.readLong()
            val location =
                intVector3Of(unpackLongX(packedLocation), unpackLongY(packedLocation), unpackLongZ(packedLocation))
            val progress = input.readByte()
            return ClientboundGameBlockBreakingProgressPacket(entityId, location, progress.toInt())
        }
    }
}