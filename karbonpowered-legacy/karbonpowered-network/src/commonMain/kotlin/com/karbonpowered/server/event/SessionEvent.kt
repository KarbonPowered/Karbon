package com.karbonpowered.server.event

import com.karbonpowered.server.Session

interface SessionEvent {
    val session: Session
    fun call(listener: SessionListener)
}