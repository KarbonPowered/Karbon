package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.common.UUID
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.math.vector.Vector2
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.util.readUUID
import com.karbonpowered.protocol.util.writeUUID
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundSpawnPlayerPacket(
    val entityId: Int,
    val uuid: UUID,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
) : MinecraftPacket {
    constructor(
        entityId: Int,
        uuid: UUID,
        position: DoubleVector3,
        rotation: Vector2<Float>
    ) : this(entityId, uuid, position.x, position.y, position.z, rotation.x, rotation.y)

    object Codec : PacketCodec<ClientboundSpawnPlayerPacket> {
        override val packetType: KClass<ClientboundSpawnPlayerPacket>
            get() = ClientboundSpawnPlayerPacket::class

        override fun decode(input: Input): ClientboundSpawnPlayerPacket {
            val entityId = input.readVarInt()
            val uuid = input.readUUID()
            val x = input.readDouble()
            val y = input.readDouble()
            val z = input.readDouble()
            val yaw = input.readByte() * 360f / 256f
            val pitch = input.readByte() * 360f / 256f
            return ClientboundSpawnPlayerPacket(entityId, uuid, x, y, z, yaw, pitch)
        }

        override fun encode(output: Output, packet: ClientboundSpawnPlayerPacket) {
            output.writeVarInt(packet.entityId)
            output.writeUUID(packet.uuid)
            output.writeDouble(packet.x)
            output.writeDouble(packet.y)
            output.writeDouble(packet.z)
            output.writeByte((packet.yaw * 256 / 360).toInt().toByte())
            output.writeByte((packet.pitch * 256 / 360).toInt().toByte())
        }
    }
}