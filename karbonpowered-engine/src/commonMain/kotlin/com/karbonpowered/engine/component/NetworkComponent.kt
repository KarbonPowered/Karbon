package com.karbonpowered.engine.component

import com.karbonpowered.engine.entity.KarbonEntity
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.engine.protocol.ProtocolEvent
import com.karbonpowered.engine.snapshot.Snapshotable
import com.karbonpowered.engine.util.Transform

open class NetworkComponent(entity: KarbonEntity) : EntityComponent(entity), Snapshotable {
    override fun copySnapshot() {

    }

    fun preSnapshotRun(transform: Transform) {

    }

    fun callProtocolEvent(event: ProtocolEvent, vararg players: KarbonPlayer) {
        players.forEach { player ->
            player.network.session.callEvent(event)
        }
    }
}