package com.karbonpowered.vanilla

import com.karbonpowered.core.MinecraftVersions
import com.karbonpowered.network.netty.NettyTcpServer
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val networkServer = NettyTcpServer("0.0.0.0", 25565) {
        VanillaProtocol(MinecraftVersions.LATEST_RELEASE, true)
    }
    VanillaServer(networkServer).start()
}