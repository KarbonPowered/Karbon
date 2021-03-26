package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.math.vector.DoubleVector2
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readVarInt
import com.karbonpowered.protocol.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.reflect.KClass

data class ClientboundGamePlayerPositionRotationPacket(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
    val yaw: Float = 0f,
    val pitch: Float = 0f,
    val teleportId: Int = 0,
    val relative: List<PositionElement> = emptyList()
) : MinecraftPacket {
    constructor(
        position: DoubleVector3,
        rotation: DoubleVector2,
        teleportId: Int = 0,
        relative: List<PositionElement> = emptyList()
    ) : this(position.x, position.y, position.z, rotation.x.toFloat(), rotation.y.toFloat(), teleportId, relative)

    enum class PositionElement(val bit: Byte) {
        X(0x01),
        Y(0x02),
        Z(0x04),
        Y_ROT(0x08),
        X_ROT(0x10);

        companion object : Iterable<PositionElement> {
            val VALUES = values()

            override fun iterator(): Iterator<PositionElement> = VALUES.iterator()
        }
    }

    companion object : MessageCodec<ClientboundGamePlayerPositionRotationPacket> {
        override val messageType: KClass<ClientboundGamePlayerPositionRotationPacket> =
            ClientboundGamePlayerPositionRotationPacket::class

        override suspend fun encode(output: Output, data: ClientboundGamePlayerPositionRotationPacket) {
            output.writeDouble(data.x)
            output.writeDouble(data.y)
            output.writeDouble(data.z)
            output.writeFloat(data.yaw)
            output.writeFloat(data.pitch)
            var flags = 0.toByte()
            data.relative.forEach { element ->
                flags = flags or element.bit
            }
            output.writeByte(flags)
            output.writeVarInt(data.teleportId)
        }

        override suspend fun decode(input: Input): ClientboundGamePlayerPositionRotationPacket {
            val x = input.readDouble()
            val y = input.readDouble()
            val z = input.readDouble()
            val yaw = input.readFloat()
            val pitch = input.readFloat()
            val flags = input.readByte()
            val relative = ArrayList<PositionElement>(PositionElement.VALUES.size)
            PositionElement.forEach { element ->
                if ((flags and element.bit) == element.bit) relative.add(element)
            }
            val teleportId = input.readVarInt()
            return ClientboundGamePlayerPositionRotationPacket(x, y, z, yaw, pitch, teleportId, relative)
        }
    }
}