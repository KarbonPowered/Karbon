package com.karbonpowered.server.cio

import com.karbonpowered.server.packet.PacketProtocol


//class CioTcpServer(
//    val rootServerJob: Job,
//    val acceptJob: Job,
//    val serverSocket: Deferred<ServerSocket>
//) : Server

data class TcpServerSettings(
    val host: String = "0.0.0.0",
    val port: Int = 25565,
    val connectionIdleTimeoutSeconds: Long = 45,
    val protocol: () -> PacketProtocol
)

//@OptIn(InternalAPI::class)
//fun CoroutineScope.cioTcpServer(
//    settings: TcpServerSettings
//) {
//    val socket = CompletableDeferred<ServerSocket>()
//    val serverLatch = Job()
//    @OptIn(ExperimentalCoroutinesApi::class)
//    val serverJob = launch(
//        context = CoroutineName("server-root-${settings.port}"),
//        start = CoroutineStart.UNDISPATCHED
//    ) {
//        serverLatch.join()
//    }
//    val acceptJob = launch(serverJob + CoroutineName("accept-${settings.port}")) {
//        aSocket(SelectorManager(coroutineContext)).tcp().bind(settings.host,settings.port).use { server ->
//            socket.complete(server)
//
//            val exceptionHandler = coroutineContext[CoroutineExceptionHandler] ?: CoroutineExceptionHandler { coroutineContext, throwable ->
//                val coroutineName = coroutineContext[CoroutineName] ?: coroutineContext.toString()
//                throwable.printStackTrace()
//            }
//            val connectionScope = CoroutineScope(coroutineContext +
//                SupervisorJob(serverJob) + exceptionHandler + CoroutineName("connection")
//            )
//
//            try {
//                while (true) {
////                    val client = server.accept()
////                    val protocol = settings.protocol()
////                    val serverSession = CioCioTcpServerSession(client.connection(), protocol , connectionScope.coroutineContext)
////                    protocol.newServerSession(serverSession)
////                    val clientJob = serverSession.startChannelRead()
////                    clientJob.invokeOnCompletion {
////                        client.close()
////                    }
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//            }
//        }
//    }
//}