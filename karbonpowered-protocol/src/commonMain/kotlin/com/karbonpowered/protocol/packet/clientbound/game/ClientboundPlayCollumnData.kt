package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.nbt.NBT
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.*
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundPlayChunkData(
    val x: Int,
    val z: Int,
    val bitmask: LongArray,
    val heightMaps: NBT,
    val biomes: IntArray,
    val data: ByteArray,
    val blockEntities: List<NBT>
) : MinecraftPacket {
    companion object : MessageCodec<ClientboundPlayChunkData> {
        override val messageType: KClass<ClientboundPlayChunkData> = ClientboundPlayChunkData::class

        override suspend fun decode(input: Input): ClientboundPlayChunkData {
            val x = input.readInt()
            val z = input.readInt()
            val bitmask = LongArray(input.readVarInt()) { input.readLong() }
            val heightMaps = requireNotNull(input.readNBT())
            val biomes = IntArray(input.readVarInt()) { input.readInt() }
            val data = ByteArray(input.readVarInt()) { input.readByte() }
            val blockEntities = List(input.readVarInt()) { requireNotNull(input.readNBT()) }
            return ClientboundPlayChunkData(
                x,z,bitmask,heightMaps,biomes,data,blockEntities
            )
        }

        override suspend fun encode(output: Output, data: ClientboundPlayChunkData) {
            output.writeInt(data.x)
            output.writeInt(data.z)
            output.writeVarInt(data.bitmask.size)
            data.bitmask.forEach {
                output.writeLong(it)
            }
            output.writeNBT(data.heightMaps)
            output.writeVarInt(data.biomes.size)
            data.biomes.forEach {
                output.writeInt(it)
            }
            output.writeVarInt(data.data.size)
            data.data.forEach {
                output.writeByte(it)
            }
            output.writeVarInt(data.blockEntities.size)
            data.blockEntities.forEach {
                output.writeNBT(it)
            }
        }
    }
}