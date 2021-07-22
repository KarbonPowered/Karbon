package com.karbonpowered.network.netty

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import io.ktor.utils.io.pool.*
import io.ktor.utils.io.streams.*
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream

private class ByteBufAsInput(
    val byteBuf: ByteBuf,
    pool: ObjectPool<ChunkBuffer>
) : Input(pool = pool) {
    override fun fill(destination: Memory, offset: Int, length: Int): Int {
        val readerIndex = byteBuf.readerIndex()

        byteBuf.readBytes(destination.buffer)
        return byteBuf.readerIndex() - readerIndex
    }

    override fun closeSource() {
    }
}

fun ByteBuf.asInput(
    pool: ObjectPool<ChunkBuffer> = ChunkBuffer.Pool
): Input = ByteBufInputStream(this).asInput(pool)
