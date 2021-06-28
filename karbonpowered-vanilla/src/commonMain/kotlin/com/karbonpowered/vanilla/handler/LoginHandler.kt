package com.karbonpowered.vanilla.handler

import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginSuccessPacket
import com.karbonpowered.server.Session
import com.karbonpowered.server.event.PacketSentEvent
import com.karbonpowered.server.event.SessionListener
import com.karbonpowered.vanilla.VanillaServer

class LoginHandler(
    val server: VanillaServer,
    val session: Session
) : SessionListener {
    override fun packetSent(event: PacketSentEvent) {
        val packet = event.packet as? ClientboundLoginSuccessPacket ?: return
        val player = server.addPlayer(packet.uniqueId, packet.username, session)
    }
}