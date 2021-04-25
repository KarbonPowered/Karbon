package com.karbonpowered.server.packet

import com.karbonpowered.server.readVarInt
import com.karbonpowered.server.writeVarInt
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

interface PacketHeader {
    val isLengthVariable: Boolean
    val lengthSize: Int

    fun lengthSize(length: Int): Int

    suspend fun readLength(input: Input, available: Int): Int
    suspend fun readPacketId(byteReadChannel: ByteReadChannel): Int

    suspend fun writeLength(byteWriteChannel: Output, length: Int)
    suspend fun writePacketId(byteWriteChannel: Output, packetId: Int)
}

class DefaultPacketHeader : PacketHeader {
    override val isLengthVariable: Boolean = true
    override val lengthSize = 5

    override fun lengthSize(length: Int): Int = when {
        length and -128 == 0 -> 1
        length and -16384 == 0 -> 2
        length and -2097152 == 0 -> 3
        length and -268435456 == 0 -> 4
        else -> 5
    }

    override suspend fun readLength(input: Input, available: Int) =
        input.readVarInt()

    override suspend fun readPacketId(byteReadChannel: ByteReadChannel) =
        byteReadChannel.readVarInt()

    override suspend fun writeLength(byteWriteChannel: Output, length: Int) {
        byteWriteChannel.writeVarInt(length)
    }

    override suspend fun writePacketId(byteWriteChannel: Output, packetId: Int) {
        byteWriteChannel.writeVarInt(packetId)
    }
}