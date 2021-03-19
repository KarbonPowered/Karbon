package com.karbonpowered.engine.network

import com.karbonpowered.network.Message
import com.karbonpowered.network.Session
import com.karbonpowered.network.protocol.Protocol
import com.karbonpowered.protocol.MinecraftProtocol
import io.ktor.network.sockets.*
import kotlinx.coroutines.channels.Channel

class KarbonSession(
    override val connection: Connection,
    override var protocol: Protocol
) : Session {

    override val outgoingMessages: Channel<Array<out Message>> = Channel(Channel.UNLIMITED)

    override suspend fun <T : Message> messageReceived(message: T) {
        (protocol as MinecraftProtocol).handlerLookupService[message::class]?.handle(this, message)
    }

    override fun disconnect() {
    }

    override fun onDisconnect() {
    }

    override fun onReady() {
    }

    override fun onInboundThrowable(throwable: Throwable) {
    }
}