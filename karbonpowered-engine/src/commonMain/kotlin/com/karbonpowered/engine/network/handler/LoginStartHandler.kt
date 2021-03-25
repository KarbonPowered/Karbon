package com.karbonpowered.engine.network.handler

import com.karbonpowered.common.UUID
import com.karbonpowered.common.uuid3Of
import com.karbonpowered.engine.Engine
import com.karbonpowered.engine.network.KarbonSession
import com.karbonpowered.network.MessageHandler
import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginSuccessPacket
import com.karbonpowered.protocol.packet.serverbound.login.ServerboundLoginStartPacket

object LoginStartHandler : MessageHandler<KarbonSession, ServerboundLoginStartPacket> {
    override suspend fun handle(session: KarbonSession, message: ServerboundLoginStartPacket) {
        Engine.server.addPlayer(message.username, session)

        session.send(
            ClientboundLoginSuccessPacket(
                uuid3Of(UUID(0, 0), message.username),
                message.username
            )
        )
    }
}