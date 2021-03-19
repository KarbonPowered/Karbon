package com.karbonpowered.network

import com.karbonpowered.network.pipeline.ConnectionHandler
import com.karbonpowered.network.pipeline.MessageDecoder
import com.karbonpowered.network.pipeline.MessageEncoder
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
    fun bind(localAddress: NetworkAddress): Job {
        serverSocket = aSocket(SelectorManager(context)).tcp().bind(localAddress)
        return GlobalScope.launch(context) {
            while (true) {
                val clientConnection = serverSocket.accept().connection()
                val input = clientConnection.input
                val connectionHandler = ConnectionHandler(this@NetworkServer)
                val messageDecoder = MessageDecoder(connectionHandler)
                val messageEncoder = MessageEncoder(connectionHandler)
                connectionHandler.connectionActive(clientConnection)
                launch {
                    while (true) {
                        val message = messageDecoder.decode(input)
                        if (message != null) {
                            connectionHandler.connectionReceive(message)
                        }
                        repeat(input.availableForRead) {
                            input.readByte()
                        }
                    }
                }.invokeOnCompletion {
                    if (it != null) {
                        connectionHandler.connectionExceptionCaught(it)
                    }
                    connectionHandler.connectionInactive(clientConnection)
                }

                launch {
                    connectionHandler.session.outgoingMessages.onEach {
                        try {
                            messageEncoder.encode(clientConnection.output, it)
                        } catch (e: Throwable) {
                            connectionHandler.connectionExceptionCaught(e)
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