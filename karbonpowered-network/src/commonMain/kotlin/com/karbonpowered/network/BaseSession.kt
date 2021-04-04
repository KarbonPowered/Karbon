package com.karbonpowered.network

import com.karbonpowered.network.protocol.Protocol
import io.ktor.network.sockets.*

open class BaseSession(
    override val connection: Connection,
    override var protocol: Protocol
) : Session {

    override fun <T : Message> messageReceived(message: T) {
    }

    override suspend fun send(vararg messages: Message) {
    }

    override fun disconnect() {
    }

    override fun onDisconnect() {
    }

    override fun onReady() {
    }

    override fun onInboundThrowable(throwable: Throwable) {
        throwable.printStackTrace()
    }
}