package com.karbonpowered.engine.network.handler

import com.karbonpowered.common.UUID
import com.karbonpowered.common.uuid3Of
import com.karbonpowered.engine.Engine
import com.karbonpowered.engine.network.GameProtocol
import com.karbonpowered.engine.network.KarbonSession
import com.karbonpowered.engine.profile.KarbonGameProfile
import com.karbonpowered.network.MessageHandler
import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginSuccessPacket
import com.karbonpowered.protocol.packet.serverbound.login.ServerboundLoginStartPacket
import kotlinx.coroutines.delay

object LoginStartHandler : MessageHandler<KarbonSession, ServerboundLoginStartPacket> {
    override suspend fun handle(session: KarbonSession, message: ServerboundLoginStartPacket) {
        val gameProfile = KarbonGameProfile(message.username, uuid3Of(UUID(0, 0), message.username))
        session.send(
            ClientboundLoginSuccessPacket(
                gameProfile.uniqueId,
                message.username
            )
        )
        delay(1000)
        session.protocol = GameProtocol(true)
        Engine.server.addPlayer(gameProfile, session)
    }
}