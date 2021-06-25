package com.karbonpowered.vanilla

import com.karbonpowered.common.UUID
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.engine.snapshot.SnapshotableHashMap
import com.karbonpowered.engine.world.KarbonWorld
import com.karbonpowered.engine.world.generator.EmptyWorldGenerator
import com.karbonpowered.engine.world.generator.WorldGenerator
import com.karbonpowered.server.Server
import com.karbonpowered.server.Session
import com.karbonpowered.server.event.server.ServerListener
import com.karbonpowered.server.event.server.SessionAddedEvent
import com.karbonpowered.vanilla.entity.VanillaPlayer
import com.karbonpowered.vanilla.handler.LoginHandler
import com.karbonpowered.vanilla.world.VanillaWorld
import kotlinx.coroutines.coroutineScope
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
class VanillaServer(
    val networkServer: Server
) : KarbonEngine(), ServerListener {
    val players = SnapshotableHashMap<UUID, KarbonPlayer>(snapshotManager)
    val worlds = SnapshotableHashMap<String, KarbonWorld>(snapshotManager)
    val defaultWorld: KarbonWorld
        get() {
            val values = worlds.values
            return values.first()
        }

    override suspend fun start() = coroutineScope {
        val duration = measureTime {
            super.start()
            networkServer.addListener(this@VanillaServer)
            networkServer.bind()
            loadWorld("world")
        }
        info("Done! ($duration) Ready for players at ${networkServer.host}:${networkServer.port}")
    }

    override fun sessionAdded(event: SessionAddedEvent) {
        event.session.addListener(LoginHandler(this, event.session))
    }

    fun addPlayer(uniqueId: UUID, username: String, session: Session): KarbonPlayer {
        info("Join player $username ($uniqueId) $session")
        val player = VanillaPlayer(this, uniqueId, username, session, defaultWorld.spawnPoint)

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

    fun loadWorld(name: String, generator: WorldGenerator = EmptyWorldGenerator): KarbonWorld {
        val snapshotWorld = worlds.map[name]
        if (snapshotWorld != null) {
            return snapshotWorld
        }
        val liveWorld = worlds.liveMap[name]
        if (liveWorld != null) {
            return liveWorld
        }
        val world = VanillaWorld(this, name, generator)
        worlds[name] = world
        check(scheduler.addAsyncManager(world)) { "Unable to add world $world to scheduler" }
        // TODO: WorldLoadEvent
        return world
    }

    override fun copySnapshotRun() {
        snapshotManager.copyAllSnapshots()
    }
}