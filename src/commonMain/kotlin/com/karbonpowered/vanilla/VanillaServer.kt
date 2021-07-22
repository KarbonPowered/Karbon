package com.karbonpowered.vanilla

import com.karbonpowered.common.DefaultUncaughtExceptionHandler
import com.karbonpowered.log.Logger
import com.karbonpowered.vanilla.network.VanillaProtocol
import com.karbonpowered.vanilla.network.VanillaProtocol756
import com.karbonpowered.vanilla.network.startReceivePipeline
import com.karbonpowered.vanilla.network.startSendPipeline
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.pipeline.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ClosedSendChannelException

class VanillaServer(
    val rootServerJob: Job,
    val acceptJob: Job,
    val serverSocket: Deferred<ServerSocket>
)

class VanillaServerSetting(
    val host: String = "0.0.0.0",
    val port: Int = 25565
)

fun CoroutineScope.vanillaServer(
    setting: VanillaServerSetting = VanillaServerSetting(),
    protocol: VanillaProtocol = VanillaProtocol756,
    inbound: PipelineInterceptor<Any, VanillaSession> = {},
    outbound: PipelineInterceptor<Any, VanillaSession> = {},
) {
    vanillaServer(
        setting, protocol,
        Pipeline(PipelinePhase("packets"), listOf(inbound)),
        Pipeline(PipelinePhase("packets"), listOf(outbound)),
    )
}

fun CoroutineScope.vanillaServer(
    setting: VanillaServerSetting = VanillaServerSetting(),
    protocol: VanillaProtocol = VanillaProtocol756,
    inbound: Pipeline<Any, VanillaSession>,
    outbound: Pipeline<Any, VanillaSession>
) {
    val socket = CompletableDeferred<ServerSocket>()
    val serverLatch = Job()

    val serverJob = launch(
        context = CoroutineName("server-root-${setting.port}"),
        start = CoroutineStart.UNDISPATCHED
    ) {
        serverLatch.join()
    }

    val selector = SelectorManager(coroutineContext)
    val logger = Logger(VanillaServer::class)

    val acceptJob = launch(serverJob + CoroutineName("accept-${setting.port}")) {
        aSocket(selector).tcp().bind(setting.host, setting.port).use { server ->
            socket.complete(server)

            val exceptionHandler =
                coroutineContext[CoroutineExceptionHandler] ?: DefaultUncaughtExceptionHandler(logger)

            val connectionScope = CoroutineScope(
                coroutineContext +
                        SupervisorJob(serverJob) +
                        exceptionHandler +
                        CoroutineName("connection")
            )

            try {
                while (true) {
                    val client = server.accept()
                    connectionScope.launch {
                        try {
                            val session = VanillaSession(client.connection(), protocol)
                            startReceivePipeline(
                                session,
                                inbound
                            ).apply {
                                invokeOnCompletion {
                                    client.close()
                                }
                            }
                            startSendPipeline(
                                session,
                                outbound
                            ).apply {
                                invokeOnCompletion {
                                    client.close()
                                }
                            }
                        } catch (closed: ClosedSendChannelException) {
                            coroutineContext.cancel()
                        } catch (closed: ClosedReceiveChannelException) {
                            coroutineContext.cancel()
                        }
                    }
                }
            } finally {
                server.close()
                server.awaitClosed()
                connectionScope.coroutineContext.cancel()
            }
        }
    }
}