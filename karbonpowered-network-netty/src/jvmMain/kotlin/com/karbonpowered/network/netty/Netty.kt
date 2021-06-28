package com.karbonpowered.network.netty

import io.netty.buffer.ByteBuf
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.ServerSocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.ResourceLeakDetector

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

fun ByteBuf.readVarInt() = com.karbonpowered.server.readVarInt { readByte() }
fun ByteBuf.writeVarInt(i: Int) = com.karbonpowered.server.writeVarInt(i) { writeByte(it.toInt()) }