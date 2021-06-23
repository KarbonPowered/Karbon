package com.karbonpowered.server.handler

import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginSuccessPacket
import com.karbonpowered.server.KarbonServer
import com.karbonpowered.server.Session
import com.karbonpowered.server.event.PacketSentEvent
import com.karbonpowered.server.event.SessionListener

class LoginHandler(
    val server: KarbonServer,
    val session: Session
) : SessionListener {
    override fun packetSent(event: PacketSentEvent) {
        val packet = event.packet as? ClientboundLoginSuccessPacket ?: return
        server.addPlayer(packet.uniqueId, packet.username, session)
    }
}