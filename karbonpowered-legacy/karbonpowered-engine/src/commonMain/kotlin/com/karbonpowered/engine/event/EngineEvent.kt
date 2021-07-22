package com.karbonpowered.engine.event

import com.karbonpowered.event.Event
import com.karbonpowered.event.EventListener

interface EngineEvent : Event {
    override fun call(listener: EventListener) {
        if (listener is EngineEventListener) {
            call(listener)
        }
    }

    fun call(listener: EngineEventListener)
}

interface EngineEventListener : EventListener {
    fun onEntityStartObservingChunks(event: EntityStartObservingChunksEvent) {}
    fun onEntityStopObservingChunks(event: EntityStopObservingChunksEvent) {}
}