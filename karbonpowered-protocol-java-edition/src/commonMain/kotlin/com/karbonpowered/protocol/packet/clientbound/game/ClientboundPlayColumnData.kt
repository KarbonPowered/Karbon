package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.io.Codec
import com.karbonpowered.nbt.NBT
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.util.BitStorage
import com.karbonpowered.protocol.util.readNBT
import com.karbonpowered.protocol.util.writeNBT
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.core.*

data class ClientboundPlayColumnData(
    val x: Int,
    val z: Int,
    val heightMaps: NBT = NBT(),
    val biomes: IntArray = IntArray(BIOME_SIZE),
    val chunks: Array<ChunkData?> = arrayOfNulls(CHUNK_COUNT),
    val blockEntities: List<NBT> = emptyList()
) : MinecraftPacket {
    data class ChunkData(
        var blockCount: Int = 0,
        var bitsPerEntry: Int = 4,
        var states: MutableList<Int> = ArrayList<Int>().apply { add(0) },
        var storage: BitStorage = BitStorage(4, 4096)
    ) {
        fun isEmpty(): Boolean = blockCount == 0

        operator fun get(x: Int, y: Int, z: Int): Int {
            val id = storage[index(x, y, z)]
            return if (bitsPerEntry <= 8) if (id >= 0 && id < states.size) states[id] else AIR else id
        }

        operator fun set(x: Int, y: Int, z: Int, state: Int) {
            var id = if (bitsPerEntry <= 8) states.indexOf(state) else state
            if (id == -1) {
                states.add(state)
                if (states.size > 1 shl bitsPerEntry) {
                    bitsPerEntry++
                    var oldStates = states
                    if (bitsPerEntry > 8) {
                        oldStates = ArrayList(states)
                        states.clear()
                        bitsPerEntry = 13
                    }
                    val oldStorage = storage
                    storage = BitStorage(bitsPerEntry, storage.size)
                    for (index in 0 until storage.size) {
                        storage[index] = if (bitsPerEntry <= 8) oldStorage[index] else oldStates[index]
                    }
                }
                id = if (bitsPerEntry <= 8) states.indexOf(state) else state
            }
            val ind: Int = index(x, y, z)
            val curr = storage[ind]
            if (state != AIR && curr == AIR) {
                blockCount++
            } else if (state == AIR && curr != AIR) {
                blockCount--
            }
            storage[ind] = id
        }

        companion object : Codec<ChunkData> {
            const val AIR = 0
            private fun index(x: Int, y: Int, z: Int): Int = y shl 8 or (z shl 4) or x

            override fun encode(output: Output, data: ChunkData) {
                output.writeShort(data.blockCount.toShort())
                output.writeByte(data.bitsPerEntry.toByte())
                if (data.bitsPerEntry <= 8) {
                    output.writeVarInt(data.states.size)
                    data.states.forEach {
                        output.writeVarInt(it)
                    }
                }
                val storageData = data.storage.data
                output.writeVarInt(storageData.size)
                storageData.forEach {
                    output.writeLong(it)
                }
            }

            override fun decode(input: Input): ChunkData {
                val blockCount = input.readShort().toInt()
                val bitsPerEntry = input.readUByte().toInt()
                val states = MutableList(if (bitsPerEntry > 8) 0 else input.readVarInt()) {
                    input.readVarInt()
                }
                val storage = BitStorage(bitsPerEntry, 4096, LongArray(input.readVarInt()) { input.readLong() })
                return ChunkData(blockCount, bitsPerEntry, states, storage)
            }
        }
    }

    companion object : PacketCodec<ClientboundPlayColumnData> {
        private const val MAX_CHUNK_Y = 20
        private const val MIN_CHUNK_Y = -4
        private const val CHUNK_COUNT = MAX_CHUNK_Y - MIN_CHUNK_Y
        private const val BIOME_SIZE = 16 * 96
        override val packetType = ClientboundPlayColumnData::class

        override fun decode(input: Input): ClientboundPlayColumnData {
            val x = input.readInt()
            val z = input.readInt()
            input.readVarInt()
            val chunkMask = input.readLong()
            val heightMaps = requireNotNull(input.readNBT())
            val biomes = IntArray(input.readVarInt()) { input.readVarInt() }
            val chunkData = buildPacket {
                writeFully(input.readBytes(input.readVarInt()))
            }
            val chunks = Array(CHUNK_COUNT) { index ->
                if (chunkMask and (1L shl index) != 0L) {
                    ChunkData.decode(chunkData)
                } else null
            }
            val blockEntities = List(input.readVarInt()) { requireNotNull(input.readNBT()) }
            return ClientboundPlayColumnData(x, z, heightMaps, biomes, chunks, blockEntities)
        }

        override fun encode(output: Output, packet: ClientboundPlayColumnData) {
            var mask = 0L
            val chunkData = buildPacket {
                packet.chunks.forEachIndexed { index, chunk ->
                    if (chunk != null && !chunk.isEmpty()) {
                        mask = mask or (1L shl index)
                        ChunkData.encode(this, chunk)
                    }
                }
            }
            output.writeInt(packet.x)
            output.writeInt(packet.z)
            output.writeVarInt(1)
            output.writeLong(mask)
            output.writeNBT(packet.heightMaps)
            output.writeVarInt(packet.biomes.size)
            packet.biomes.forEach {
                output.writeVarInt(it)
            }
            output.writeVarInt(chunkData.remaining.toInt())
            output.writePacket(chunkData)
            output.writeVarInt(packet.blockEntities.size)
            packet.blockEntities.forEach {
                output.writeNBT(it)
            }
        }
    }
}
