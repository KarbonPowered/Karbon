package com.karbonpowered.vanilla

import com.karbonpowered.common.UUID
import com.karbonpowered.data.ResourceKey
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.player.KarbonPlayer
import com.karbonpowered.engine.player.component.PlayerObserveChunksComponent
import com.karbonpowered.engine.snapshot.SnapshotableHashMap
import com.karbonpowered.engine.world.LoadOption
import com.karbonpowered.engine.world.generator.FlatWorldGenerator
import com.karbonpowered.server.Server
import com.karbonpowered.server.Session
import com.karbonpowered.server.event.server.ServerListener
import com.karbonpowered.server.event.server.SessionAddedEvent
import com.karbonpowered.server.event.server.SessionRemovedEvent
import com.karbonpowered.text.LiteralText
import com.karbonpowered.vanilla.handler.ChatHandler
import com.karbonpowered.vanilla.handler.LoginHandler
import com.karbonpowered.vanilla.player.VanillaMovementComponent
import com.karbonpowered.vanilla.player.VanillaPlayer
import com.karbonpowered.vanilla.world.VanillaChunkIterator
import kotlinx.coroutines.coroutineScope
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
class VanillaServer(
    val networkServer: Server
) : KarbonEngine(), ServerListener {
    val players = SnapshotableHashMap<UUID, VanillaPlayer>(snapshotManager)
    val playerSessions = SnapshotableHashMap<Session, UUID>(snapshotManager)
    val defaultWorld get() = worldManager.defaultWorld

    override suspend fun start() = coroutineScope {
        val duration = measureTime {
            super.start()
            networkServer.addListener(this@VanillaServer)
            networkServer.bind()
            worldManager.loadWorld(ResourceKey("karbon", "world"), FlatWorldGenerator(1))
        }
        info("Done! ($duration) Ready for players at ${networkServer.host}:${networkServer.port}")
    }

    override fun sessionAdded(event: SessionAddedEvent) {
        event.session.addListener(LoginHandler(this, event.session))
    }

    fun addPlayer(uniqueId: UUID, username: String, session: Session): KarbonPlayer {
        info("Join player $username ($uniqueId) $session")

        val entity = defaultWorld.spawnEntity(defaultWorld.spawnPoint.position, uniqueId)
        val player = VanillaPlayer(this, uniqueId, username, session)
        val oldPlayer = players.put(uniqueId, player)

        if (oldPlayer != null && oldPlayer.network.session.isConnected) {
            oldPlayer.network.session.disconnect("Login from another client")
        }

        playerSessions[session] = uniqueId

        entity.observer.syncDistance = 8
        entity.observer.setObserver(true, VanillaChunkIterator(entity.observer.syncDistance, 0, 0))
        entity.components.add(PlayerObserveChunksComponent(player))
        entity.components.add(VanillaMovementComponent(player, entity))

        player.transformProvider = entity.physics
        player.network.session.addListener(ChatHandler(this, player))

        scheduler.addAsyncManager(player)
        broadcast("§e${player.username} joined the game")

        return player
    }

    override fun sessionRemoved(event: SessionRemovedEvent) {
        val session = event.session
        val uniqueId = playerSessions.remove(session) ?: return
        val player = players.remove(uniqueId) ?: return
        scheduler.removeAsyncManager(player)

        val region = player.transformProvider.transform.position.region(worldManager, LoadOption.NO_LOAD)
        region?.entityManager?.removeEntity(uniqueId)

        broadcast("§e${player.username} left the game")
    }

    fun broadcast(message: String) {
        info(message)
        players.values.forEach { player ->
            player.sendMessage(LiteralText(message))
        }
    }

    override fun copySnapshotRun() {
        snapshotManager.copyAllSnapshots()
    }
}