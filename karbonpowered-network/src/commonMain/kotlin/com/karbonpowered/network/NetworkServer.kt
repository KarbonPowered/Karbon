package com.karbonpowered.network

import com.karbonpowered.network.pipeline.MessageDecoder
import com.karbonpowered.network.pipeline.MessageEncoder
import com.karbonpowered.network.pipeline.MessageHandler
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

abstract class NetworkServer(
    val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ConnectionManager, Closeable {
    val context = Job()
    lateinit var serverSocket: ServerSocket
        private set

    @OptIn(InternalAPI::class)
    fun bind(localAddress: NetworkAddress) {
        serverSocket = aSocket(SelectorManager(context)).tcp().bind(localAddress)
        GlobalScope.launch(context) {
            while (true) {
                val clientConnection = serverSocket.accept().connection()
                val input = clientConnection.input
                val messageHandler = MessageHandler(this@NetworkServer)
                val messageDecoder = MessageDecoder(messageHandler)
                val messageEncoder = MessageEncoder(messageHandler)
                messageHandler.connectionActive(clientConnection)
                launch {
                    while (true) {
                        val message = messageDecoder.decode(input)
                        if (message != null) {
                            messageHandler.connectionReceive(message)
                        }
                        repeat(input.availableForRead) {
                            input.readByte()
                        }
                    }
                }.invokeOnCompletion {
                    if (it != null) {
                        messageHandler.connectionExceptionCaught(it)
                    }
                    messageHandler.connectionInactive(clientConnection)
                }

                launch {
                    messageHandler.session.outgoingMessages.onEach {
                        try {
                            messageEncoder.encode(clientConnection.output, it)
                        } catch (e: Throwable) {
                            messageHandler.connectionExceptionCaught(e)
                        }
                    }.collect()
                }
            }
        }
    }

    override fun close() {
        context.cancel()
    }
}