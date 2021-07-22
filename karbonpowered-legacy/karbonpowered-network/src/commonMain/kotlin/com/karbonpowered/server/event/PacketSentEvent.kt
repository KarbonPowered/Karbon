package com.karbonpowered.server.event

import com.karbonpowered.server.Session
import com.karbonpowered.server.packet.Packet

data class PacketSentEvent(
    override val session: Session,
    val packet: Packet
) : SessionEvent {
    @Suppress("UNCHECKED_CAST")
    fun <T : Packet> packet() = packet as? T
        ?: throw IllegalStateException("Tried to get packet as the wrong type. Actual type: ${packet::class}")

    override fun call(listener: SessionListener) {
        listener.packetSent(this)
    }
}

