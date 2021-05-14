package com.karbonpowered.server.event

import com.karbonpowered.server.Session
import com.karbonpowered.server.packet.Packet

interface PacketSendingEvent : SessionEvent {
    val session: Session
    var packet: Packet
    var isCancelled: Boolean

    fun <T : Packet> packet() = packet as? T
        ?: throw IllegalStateException("Tried to get packet as the wrong type. Actual type: ${packet::class}")

    override fun call(listener: SessionListener) {
        listener.packetSending(this)
    }
}


data class PacketSendingEventImpl(
    override val session: Session,
    override var packet: Packet,
    override var isCancelled: Boolean = false
) : PacketSendingEvent

