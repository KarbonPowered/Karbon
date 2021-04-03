package com.karbonpowered.network

import com.karbonpowered.network.pipeline.ConnectionHandler
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*

abstract class NetworkServer(
    val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ConnectionManager, Closeable {
    val context = Job()
    lateinit var serverSocket: ServerSocket
        private set
    val connectionHandler = ConnectionHandler(this@NetworkServer)

    @OptIn(InternalAPI::class)
    fun bind(localAddress: NetworkAddress): Job {
        serverSocket = aSocket(SelectorManager(context)).tcp().bind(localAddress)
        return GlobalScope.launch(context) {
            while (true) {
                val clientConnection = serverSocket.accept().connection()
                connectionHandler.connectionActive(clientConnection)
            }
        }
    }

    override fun close() {
        context.cancel()
    }
}