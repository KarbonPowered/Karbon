package com.karbonpowered.engine.network.handler

import com.karbonpowered.network.MessageHandler
import com.karbonpowered.network.Session
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusPongPacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusPingPacket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object StatusPingHandler : MessageHandler<Session, ServerboundStatusPingPacket> {
    override fun handle(session: Session, message: ServerboundStatusPingPacket) {
        GlobalScope.launch {
            session.send(ClientboundStatusPongPacket(message.payload))
        }
    }
}