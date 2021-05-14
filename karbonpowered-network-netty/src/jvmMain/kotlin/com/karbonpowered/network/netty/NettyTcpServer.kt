package com.karbonpowered.network.netty

import com.karbonpowered.server.Server
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer

class NettyTcpServer : Server {
    val eventLoopGroup = Netty.createEventLoopGroup()

    fun bind() {
        ServerBootstrap().apply {
            channel(Netty.serverSocketChannel())
            childHandler(object : ChannelInitializer<Channel>() {
                override fun initChannel(ch: Channel) {

                }
            })
        }
    }
}