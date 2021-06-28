package com.karbonpowered.vanilla

import com.karbonpowered.common.UUID
import com.karbonpowered.data.ResourceKey
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.engine.snapshot.SnapshotableHashMap
import com.karbonpowered.engine.world.generator.FlatWorldGenerator
import com.karbonpowered.server.Server
import com.karbonpowered.server.Session
import com.karbonpowered.server.event.server.ServerListener
import com.karbonpowered.server.event.server.SessionAddedEvent
import com.karbonpowered.vanilla.handler.LoginHandler
import kotlinx.coroutines.coroutineScope
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
class VanillaServer(
    val networkServer: Server
) : KarbonEngine(), ServerListener {
    val players = SnapshotableHashMap<UUID, KarbonPlayer>(snapshotManager)
    val defaultWorld get() = worldManager.defaultWorld

    override suspend fun start() = coroutineScope {
        val duration = measureTime {
            super.start()
            networkServer.addListener(this@VanillaServer)
            networkServer.bind()
            worldManager.loadWorld(ResourceKey("karbon", "world"), FlatWorldGenerator(1))
        }
        info("Done! ($duration) Ready for players at ${networkServer.host}:${networkServer.port}")

        val testEntity = defaultWorld.spawnEntity(defaultWorld.spawnPoint.position)
        testEntity.observer.isObserver = true
    }

    override fun sessionAdded(event: SessionAddedEvent) {
        event.session.addListener(LoginHandler(this, event.session))
    }

    fun addPlayer(uniqueId: UUID, username: String, session: Session): KarbonPlayer {
        info("Join player $username ($uniqueId) $session")

        val player = KarbonPlayer(uniqueId, username, session)
        val entity = defaultWorld.spawnEntity(defaultWorld.spawnPoint.position)
        entity.observer.isObserver = true

        val oldPlayer = players.put(uniqueId, player)

        if (oldPlayer != null && oldPlayer.session.isConnected) {
            oldPlayer.session.disconnect("Login from another client")
        }

        return player
    }

    override fun copySnapshotRun() {
        snapshotManager.copyAllSnapshots()
    }
}