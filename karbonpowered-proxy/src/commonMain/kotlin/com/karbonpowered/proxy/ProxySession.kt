package com.karbonpowered.proxy

import com.karbonpowered.network.BaseSession
import com.karbonpowered.protocol.MinecraftProtocol
import io.ktor.network.sockets.*
import kotlinx.coroutines.flow.onEach

class ProxySession(
    override val connection: Connection,
    override var protocol: MinecraftProtocol
) : BaseSession(connection, protocol) {
    init {
        incomingMessages.onEach {
            println(it)
        }
    }
}