package com.karbonpowered.server.event

import com.karbonpowered.server.Session
import com.karbonpowered.server.packet.Packet

interface PacketReceivedEvent : SessionEvent {
    val session: Session
    val packet: Packet

    fun <T : Packet> packet() = packet as? T
        ?: throw IllegalStateException("Tried to get packet as the wrong type. Actual type: ${packet::class}")

    override fun call(listener: SessionListener) {
        listener.packetReceived(this)
    }
}


data class PacketReceivedEventImpl(
    override val session: Session,
    override val packet: Packet
) : PacketReceivedEvent

