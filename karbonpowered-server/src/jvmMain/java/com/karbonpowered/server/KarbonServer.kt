package com.karbonpowered.server

import com.karbonpowered.common.UUID
import com.karbonpowered.core.MinecraftVersions
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.network.netty.NettyTcpServer
import com.karbonpowered.server.event.server.ServerListener
import com.karbonpowered.server.event.server.SessionAddedEvent
import com.karbonpowered.server.handler.LoginHandler
import kotlinx.coroutines.runBlocking
import kotlin.time.measureTime

class KarbonServer(
    host: String = "0.0.0.0",
    port: Int = 25565,
) : KarbonEngine(), ServerListener {
    val networkServer = NettyTcpServer(host, port) {
        VanillaProtocol(MinecraftVersions.LATEST_RELEASE, true).apply {

        }
    }
    val players = mutableMapOf<UUID, KarbonPlayer>()

    override fun start() {
        val duration = measureTime {
            super.start()
            runBlocking {
                networkServer.addListener(this@KarbonServer)
                networkServer.bind()
            }
        }
        info("Done! ($duration) Ready for players at ${networkServer.host}:${networkServer.port}")
    }

    override fun sessionAdded(event: SessionAddedEvent) {
        event.session.addListener(LoginHandler(this, event.session))
    }

    fun addPlayer(uniqueId: UUID, username: String, session: Session) {
        info("Join player $username ($uniqueId) $session")
    }
}