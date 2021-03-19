package com.karbonpowered.network

import com.karbonpowered.network.protocol.Protocol
import io.ktor.network.sockets.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

open class BaseSession(
    override val connection: Connection,
    override val protocol: Protocol
) : Session {
    protected val outgoingMessagesChannel = Channel<Message>()
    protected val incomingMessagesChannel = Channel<Message>()

    override val outgoingMessages: Flow<Message> = outgoingMessagesChannel.receiveAsFlow()
    override val incomingMessages: Flow<Message> = incomingMessagesChannel.receiveAsFlow()

    override suspend fun <T : Message> messageReceived(message: T) {
        incomingMessagesChannel.send(message)
    }

    override suspend fun send(vararg messages: Message) {
        messages.forEach { message ->
            outgoingMessagesChannel.send(message)
        }
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