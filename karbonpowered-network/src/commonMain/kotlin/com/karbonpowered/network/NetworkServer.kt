package com.karbonpowered.network

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

    @OptIn(InternalAPI::class)
    fun bind(localAddress: NetworkAddress): Job {
        serverSocket = aSocket(SelectorManager(context)).tcp().bind(localAddress)
        return GlobalScope.launch(context) {
            println("Server started at $localAddress")
            while (true) {
                val clientConnection = serverSocket.accept().connection()
                val session = newSession(clientConnection)
                session.onReady()
                launch {
                    while (!clientConnection.input.isClosedForRead) {
                        delay(1)
                    }
                    session.onDisconnect()
                    sessionInactivated(session)
                }
            }
        }
    }

    override fun close() {
        context.cancel()
    }
}