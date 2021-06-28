package com.karbonpowered.network.netty

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import io.ktor.utils.io.pool.*
import io.netty.buffer.ByteBuf

private class ByteBufAsInput(
    val byteBuf: ByteBuf,
    pool: ObjectPool<ChunkBuffer>
) : Input(pool = pool) {
    override fun fill(destination: Memory, offset: Int, length: Int): Int {
        val readerIndex = byteBuf.readerIndex()
        byteBuf.readBytes(destination.buffer.slice(offset, length))
        return byteBuf.readerIndex() - readerIndex
    }

    override fun closeSource() {
    }
}

fun ByteBuf.asInput(
    pool: ObjectPool<ChunkBuffer> = ChunkBuffer.Pool
): Input = ByteBufAsInput(this, pool)
