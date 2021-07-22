package com.karbonpowered.server.event

import com.karbonpowered.server.Session

data class PacketErrorEvent(
    override val session: Session,
    val cause: Throwable,
    var shouldSuppress: Boolean = false
) : SessionEvent {
    override fun call(listener: SessionListener) {
        listener.packetError(this)
    }
}