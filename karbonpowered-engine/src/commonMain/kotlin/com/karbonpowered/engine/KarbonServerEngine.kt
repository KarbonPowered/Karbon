package com.karbonpowered.engine

import com.karbonpowered.common.UUID
import com.karbonpowered.engine.player.KarbonPlayer
import com.karbonpowered.engine.snapshot.SnapshotableHashMap
import com.karbonpowered.server.Session

abstract class KarbonServerEngine : KarbonEngine() {
    open val players = SnapshotableHashMap<UUID, KarbonPlayer>(snapshotManager)
    val playerSessions = SnapshotableHashMap<Session, UUID>(snapshotManager)
}