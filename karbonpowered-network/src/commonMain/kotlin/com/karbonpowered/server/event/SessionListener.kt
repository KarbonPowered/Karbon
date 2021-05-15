package com.karbonpowered.server.event

import com.karbonpowered.server.packet.Packet

interface SessionListener {
    fun disconnected(event: DisconnectedEvent) {}
    fun disconnecting(event: DisconnectingEvent) {}
    fun packetSending(event: PacketSendingEvent) {}
    fun packetReceived(event: PacketReceivedEvent) {}
    fun connected(event: SessionConnectedEvent) {}
    fun packetSent(event: PacketSentEvent) {}
    fun packetError(event: PacketErrorEvent) {}
}

class SessionListenerBuilder : SessionListener {
    private var packetReceived: (Packet) -> Unit = {}
    private var packetError: (PacketErrorEvent) -> Unit = {}

    fun onPacketReceived(blocK: (Packet) -> Unit) {
        packetReceived = blocK
    }

    override fun packetReceived(event: PacketReceivedEvent) {
        packetReceived.invoke(event.packet)
    }

    fun onPacketError(block: (PacketErrorEvent) -> Unit) {
        packetError = block
    }

    override fun packetError(event: PacketErrorEvent) {
        packetError.invoke(event)
    }
}