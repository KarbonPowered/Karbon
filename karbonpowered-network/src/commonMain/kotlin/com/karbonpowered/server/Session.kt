package com.karbonpowered.server

import com.karbonpowered.server.event.SessionEvent
import com.karbonpowered.server.event.SessionListener
import com.karbonpowered.server.packet.Packet
import com.karbonpowered.server.packet.PacketProtocol
import kotlinx.coroutines.CoroutineScope

interface Session : CoroutineScope {
    val isConnected: Boolean
    var packetProtocol: PacketProtocol
    val listeners: Collection<SessionListener>

    fun addListener(listener: SessionListener)
    fun removeListener(listener: SessionListener)
    fun sendPacket(packet: Packet, flush: Boolean = true)
    fun sendPackets(vararg packets: Packet, flush: Boolean = true)
    fun sendPackets(packets: Iterable<Packet>, flush: Boolean = true)
    fun disconnect(reason: String = "Connection closed", cause: Throwable? = null)
    fun callEvent(event: SessionEvent)
    fun exceptionCaught(cause: Throwable)
}