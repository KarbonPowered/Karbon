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
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

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
                val connectionScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
                connectionScope.launch {
                    while (!input.isClosedForWrite) {
                        try {
                            val message = messageDecoder.decode(input)
                            if (message != null) {
                                connectionHandler.connectionReceive(message)
                            }
                        } catch (e: ClosedReceiveChannelException) {
                            break
                        }
                    }
                }.invokeOnCompletion {
                    if (it != null) {
                        connectionHandler.connectionExceptionCaught(it)
                    }
                    connectionHandler.connectionInactive(clientConnection)
                }

                connectionHandler.session.outgoingMessages.receiveAsFlow().onEach { messages ->
                    if (!clientConnection.output.isClosedForWrite) {
                        messages.forEach { message ->
                            try {
                                messageEncoder.encode(clientConnection.output, message)
                            } catch (e: Throwable) {
                                connectionHandler.connectionExceptionCaught(e)
                            }
                        }
                        clientConnection.output.flush()
                    }
                }.launchIn(connectionScope)
            }
        }
    }

    override fun close() {
        context.cancel()
    }
}