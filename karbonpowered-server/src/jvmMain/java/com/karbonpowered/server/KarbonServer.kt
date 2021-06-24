package com.karbonpowered.server

import com.karbonpowered.common.UUID
import com.karbonpowered.core.MinecraftVersions
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.network.netty.NettyTcpServer
import com.karbonpowered.server.event.server.ServerListener
import com.karbonpowered.server.event.server.SessionAddedEvent
import com.karbonpowered.server.handler.LoginHandler
import kotlinx.coroutines.coroutineScope
import kotlin.time.measureTime

class KarbonServer(
    host: String = "0.0.0.0",
    port: Int = 25565,
) : KarbonEngine(), ServerListener {
    val networkServer = NettyTcpServer(host, port) { VanillaProtocol(MinecraftVersions.LATEST_RELEASE, true) }
    val players = mutableMapOf<UUID, KarbonPlayer>()

    override suspend fun start() = coroutineScope {
        val duration = measureTime {
            super.start()
            networkServer.addListener(this@KarbonServer)
            networkServer.bind()
        }
        info("Done! ($duration) Ready for players at ${networkServer.host}:${networkServer.port}")
    }

    override fun sessionAdded(event: SessionAddedEvent) {
        event.session.addListener(LoginHandler(this, event.session))
    }

    fun addPlayer(uniqueId: UUID, username: String, session: Session): KarbonPlayer {
        info("Join player $username ($uniqueId) $session")
        val player = KarbonPlayer(this, uniqueId, username, session)

        val oldPlayer = players.put(uniqueId, player)
        if (oldPlayer != null && oldPlayer.network.session.isConnected) {
            oldPlayer.network.session.disconnect("Login from another client")
        }

        val physics = player.physics
        val world = physics.transformLive.position.world
        world.spawnEntity(player)
        world.addPlayer(player)

        player.network.forceSync()

        return player
    }
}