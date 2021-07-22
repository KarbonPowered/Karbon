package com.karbonpowered.engine.player.component

import com.karbonpowered.engine.component.Component
import com.karbonpowered.engine.event.EngineEventListener
import com.karbonpowered.engine.event.EntityStartObservingChunksEvent
import com.karbonpowered.engine.event.EntityStopObservingChunksEvent
import com.karbonpowered.engine.player.KarbonPlayer
import com.karbonpowered.engine.protocol.ProtocolEventListener
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

open class PlayerObserveChunksComponent(
    val player: KarbonPlayer
) : Component(), EngineEventListener, ProtocolEventListener {

    @OptIn(ExperimentalTime::class)
    override suspend fun tick(duration: Duration) {
    }

    override fun onEntityStartObservingChunks(event: EntityStartObservingChunksEvent) {
        if (event.observer.uniqueId == player.uniqueId) {
            player.network.addChunks(event.observed)
        }
    }

    override fun onEntityStopObservingChunks(event: EntityStopObservingChunksEvent) {
        if (event.observer.uniqueId == player.uniqueId) {
            player.network.removeChunks(event.observed)
        }
    }

    override fun onAttached() {
        player.engine.eventManager.registerListener(this)
    }
}