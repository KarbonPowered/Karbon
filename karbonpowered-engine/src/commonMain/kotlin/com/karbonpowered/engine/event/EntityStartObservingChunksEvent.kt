package com.karbonpowered.engine.event

import com.karbonpowered.engine.entity.KarbonEntity
import com.karbonpowered.engine.world.reference.ChunkReference

data class EntityStartObservingChunksEvent(
    val observer: KarbonEntity,
    val observed: Set<ChunkReference>
) : EngineEvent {
    override fun call(listener: EngineEventListener) {
        listener.onEntityStartObservingChunks(this)
    }
}