package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.common.UUID
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.math.vector.FloatVector2
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.*
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
            rotation: FloatVector2
    ) : this(entityId, uuid, position.x, position.y, position.z, rotation.x, rotation.y)

    companion object : MessageCodec<ClientboundSpawnPlayerPacket> {
        override val messageType: KClass<ClientboundSpawnPlayerPacket>
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

        override fun encode(output: Output, data: ClientboundSpawnPlayerPacket) {
            output.writeVarInt(data.entityId)
            output.writeUUID(data.uuid)
            output.writeDouble(data.x)
            output.writeDouble(data.y)
            output.writeDouble(data.z)
            output.writeByte((data.yaw * 256 / 360).toInt().toByte())
            output.writeByte((data.pitch * 256 / 360).toInt().toByte())
        }
    }
}