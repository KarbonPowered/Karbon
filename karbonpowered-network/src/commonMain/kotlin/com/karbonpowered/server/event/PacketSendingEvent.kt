package com.karbonpowered.server.event

import com.karbonpowered.server.Session
import com.karbonpowered.server.packet.Packet

data class PacketSendingEvent(
    override val session: Session,
    var packet: Packet,
    var isCancelled: Boolean = false
) : SessionEvent {
    fun <T : Packet> packet() = packet as? T
        ?: throw IllegalStateException("Tried to get packet as the wrong type. Actual type: ${packet::class}")

    override fun call(listener: SessionListener) {
        listener.packetSending(this)
    }
}

