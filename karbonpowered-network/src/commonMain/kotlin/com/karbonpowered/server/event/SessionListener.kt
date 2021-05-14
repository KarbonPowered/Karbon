package com.karbonpowered.server.event

interface SessionListener {
    fun disconnected(event: DisconnectedEvent) {}
    fun disconnecting(event: DisconnectingEvent) {}
    fun packetSending(event: PacketSendingEvent) {}
    fun packetReceived(event: PacketReceivedEvent) {}
    fun connected(event: ConnectedEvent) {}
    fun packetSent(event: PacketSentEvent) {}
}