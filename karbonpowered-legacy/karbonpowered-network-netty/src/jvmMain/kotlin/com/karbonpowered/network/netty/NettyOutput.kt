package com.karbonpowered.network.netty

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import io.ktor.utils.io.pool.*
import io.netty.buffer.ByteBuf

private class ByteBufAsOutput(
    pool: ObjectPool<ChunkBuffer>,
    val byteBuf: ByteBuf
) : Output(pool) {
    override fun flush(source: Memory, offset: Int, length: Int) {
        val slice = source.buffer.duplicate().slice(offset, length)
        while (slice.hasRemaining()) {
            byteBuf.writeBytes(slice)
        }
    }

    override fun closeDestination() {
    }
}

fun ByteBuf.asOutput(
    pool: ObjectPool<ChunkBuffer> = ChunkBuffer.Pool
): Output = ByteBufAsOutput(pool, this)