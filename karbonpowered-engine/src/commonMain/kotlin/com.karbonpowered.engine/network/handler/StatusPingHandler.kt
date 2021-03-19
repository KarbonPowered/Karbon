package com.karbonpowered.engine.network.handler

import com.karbonpowered.network.MessageHandler
import com.karbonpowered.network.Session
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusPongPacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusPingPacket

object StatusPingHandler : MessageHandler<Session, ServerboundStatusPingPacket> {
    override suspend fun handle(session: Session, message: ServerboundStatusPingPacket) {
        session.send(ClientboundStatusPongPacket(message.payload))
    }
}