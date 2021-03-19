package com.karbonpowered.proxy.handler

import com.karbonpowered.network.MessageHandler
import com.karbonpowered.network.Session
import com.karbonpowered.protocol.ServerboundStatusRequest
import com.karbonpowered.protocol.packet.serverbound.handshake.ServerboundHandshakePacket

object HandshakeHandler : MessageHandler<Session, ServerboundHandshakePacket> {
    override fun handle(session: Session, message: ServerboundHandshakePacket) {
        if (message.nextState == 1) {
            session.protocol = ServerboundStatusRequest
        }
    }
}