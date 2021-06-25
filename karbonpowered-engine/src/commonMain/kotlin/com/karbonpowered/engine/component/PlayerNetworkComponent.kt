package com.karbonpowered.engine.component

import com.karbonpowered.common.UUID
import com.karbonpowered.engine.entity.KarbonEntity
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.engine.protocol.ProtocolEvent
import com.karbonpowered.engine.world.KarbonChunk
import com.karbonpowered.server.Session
import kotlinx.atomicfu.atomic

open class PlayerNetworkComponent(
    player: KarbonPlayer,
    session: Session
) : NetworkComponent(player) {
    val syncDistance = 10
    private var sync: Boolean = false
    var session by atomic(session)

    val synchronizedEntities = HashSet<UUID>()

    fun hasSpawned(entity: KarbonEntity) =
        synchronizedEntities.contains(entity.uniqueId)

    fun forceSync() {
        sync = true
    }

    fun isObservedChunk(chunk: KarbonChunk): Boolean = true

    fun callProtocolEvent(event: ProtocolEvent) {
        session.callEvent(event)
    }
}