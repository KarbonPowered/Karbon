package com.karbonpowered.engine.network.handler

import com.karbonpowered.engine.network.LoginProtocol
import com.karbonpowered.engine.network.StatusProtocol
import com.karbonpowered.network.MessageHandler
import com.karbonpowered.network.Session
import com.karbonpowered.protocol.packet.serverbound.handshake.ServerboundHandshakePacket

object HandshakeHandler : MessageHandler<Session, ServerboundHandshakePacket> {
    override suspend fun handle(session: Session, message: ServerboundHandshakePacket) {
        when (message.nextState) {
            1 -> {
                session.protocol = StatusProtocol(true)
            }
            2 -> {
                session.protocol = LoginProtocol(true)
            }
        }
    }
}