package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.math.vector.DoubleVector2
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readBoolean
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeBoolean
import com.karbonpowered.server.writeVarInt
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
    val relative: List<PositionElement> = emptyList(),
    val teleportId: Int = 0,
    val shouldDismount: Boolean = false
) : MinecraftPacket {
    constructor(
        position: DoubleVector3,
        rotation: DoubleVector2,
        relative: List<PositionElement> = emptyList(),
        teleportId: Int = 0,
        shouldDismount: Boolean = false
    ) : this(
        position.x,
        position.y,
        position.z,
        rotation.x.toFloat(),
        rotation.y.toFloat(),
        relative,
        teleportId,
        shouldDismount
    )

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

    object Codec754 : PacketCodec<ClientboundGamePlayerPositionRotationPacket> {
        override val packetType: KClass<ClientboundGamePlayerPositionRotationPacket> =
            ClientboundGamePlayerPositionRotationPacket::class

        override fun encode(output: Output, packet: ClientboundGamePlayerPositionRotationPacket) {
            output.writeDouble(packet.x)
            output.writeDouble(packet.y)
            output.writeDouble(packet.z)
            output.writeFloat(packet.yaw)
            output.writeFloat(packet.pitch)
            var flags = 0.toByte()
            packet.relative.forEach { element ->
                flags = flags or element.bit
            }
            output.writeByte(flags)
            output.writeVarInt(packet.teleportId)
        }

        override fun decode(input: Input): ClientboundGamePlayerPositionRotationPacket {
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
            return ClientboundGamePlayerPositionRotationPacket(
                x,
                y,
                z,
                yaw,
                pitch,
                relative,
                teleportId
            )
        }
    }

    object Codec : PacketCodec<ClientboundGamePlayerPositionRotationPacket> {
        override val packetType: KClass<ClientboundGamePlayerPositionRotationPacket> =
            ClientboundGamePlayerPositionRotationPacket::class

        override fun encode(output: Output, packet: ClientboundGamePlayerPositionRotationPacket) {
            output.writeDouble(packet.x)
            output.writeDouble(packet.y)
            output.writeDouble(packet.z)
            output.writeFloat(packet.yaw)
            output.writeFloat(packet.pitch)
            var flags = 0.toByte()
            packet.relative.forEach { element ->
                flags = flags or element.bit
            }
            output.writeByte(flags)
            output.writeVarInt(packet.teleportId)
            output.writeBoolean(packet.shouldDismount)
        }

        override fun decode(input: Input): ClientboundGamePlayerPositionRotationPacket {
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
            val shouldDismount = input.readBoolean()
            return ClientboundGamePlayerPositionRotationPacket(
                x,
                y,
                z,
                yaw,
                pitch,
                relative,
                teleportId,
                shouldDismount
            )
        }
    }
}