package com.karbonpowered.server.event

interface SessionEvent {
    fun call(listener: SessionListener)
}