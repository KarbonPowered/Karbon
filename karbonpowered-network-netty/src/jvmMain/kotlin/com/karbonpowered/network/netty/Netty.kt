package com.karbonpowered.network.netty

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufOutputStream
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.ServerSocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.ResourceLeakDetector
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object Netty {
    init {
        // By default, Netty allocates 16MiB arenas for the PooledByteBufAllocator. This is too much
        // memory for Minecraft, which imposes a maximum packet size of 2MiB! We'll use 4MiB as a more
        // sane default.
        //
        // Note: io.netty.allocator.pageSize << io.netty.allocator.maxOrder is the formula used to
        // compute the chunk size. We lower maxOrder from its default of 11 to 9. (We also use a null
        // check, so that the user is free to choose another setting if need be.)
        if (System.getProperty("io.netty.allocator.maxOrder") == null) {
            System.setProperty("io.netty.allocator.maxOrder", "9")
        }

        // Disable the resource leak detector by default as it reduces performance. Allow the user to
        // override this if desired.
        if (System.getProperty("io.netty.leakDetection.level") == null) {
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED)
        }
    }

    fun createEventLoopGroup(): EventLoopGroup = if (Epoll.isAvailable()) {
        EpollEventLoopGroup()
    } else {
        NioEventLoopGroup()
    }

    fun serverSocketChannel(): Class<out ServerSocketChannel> = if (Epoll.isAvailable()) {
        EpollServerSocketChannel::class.java
    } else {
        NioServerSocketChannel::class.java
    }
}

fun ByteBuf.asInput() = ByteBufInput(this)

@JvmInline
value class ByteBufInput(val byteBuf: ByteBuf) : Input {
    override var byteOrder: ByteOrder
        get() = throw UnsupportedOperationException()
        set(_) {}
    override val endOfInput: Boolean
        get() = !byteBuf.isReadable

    override fun close() {
        byteBuf.release()
    }

    override fun discard(n: Long): Long {
        val size = minOf(n, byteBuf.readableBytes().toLong())
        byteBuf.skipBytes(size.toInt())
        return size
    }

    override fun peekTo(destination: Memory, destinationOffset: Long, offset: Long, min: Long, max: Long): Long {
        val buffer = destination.buffer
        val bytes = ByteArray(minOf(byteBuf.readableBytes(), max.toInt()))
        val readerIndex = byteBuf.readerIndex()
        byteBuf.readBytes(bytes)
        byteBuf.readerIndex(readerIndex)
        buffer.put(buffer.position() + destinationOffset.toInt(), bytes)
        return bytes.size.toLong()
    }

    override fun readByte(): Byte = byteBuf.readByte()

    override fun tryPeek(): Int = if (byteBuf.isReadable) {
        byteBuf.getByte(byteBuf.readerIndex() + 1).toInt()
    } else {
        -1
    }
}

fun ByteBuf.asOutput() = ByteBufOutputStream(this).asOutput()
fun ByteBuf.readVarInt() = com.karbonpowered.server.readVarInt { readByte() }
fun ByteBuf.writeVarInt(i: Int) = com.karbonpowered.server.writeVarInt(i) { writeByte(it.toInt()) }

suspend fun <T> io.netty.util.concurrent.Future<T>.suspendAwait() = suspendCancellableCoroutine<T> { continuation ->
    continuation.invokeOnCancellation {
        cancel(true)
    }
    addListener {
        if (it.isSuccess) {
            continuation.resume(get())
        } else {
            continuation.resumeWithException(it.cause())
        }
    }
}

suspend fun ChannelFuture.suspendAwait() = suspendCancellableCoroutine<Channel> { continuation ->
    continuation.invokeOnCancellation {
        cancel(true)
    }
    addListener {
        if (it.isSuccess) {
            continuation.resume(channel())
        } else {
            continuation.resumeWithException(it.cause())
        }
    }
}