package com.karbonpowered.engine.network.handler

import com.karbonpowered.engine.Engine
import com.karbonpowered.engine.network.GameProtocol
import com.karbonpowered.engine.network.KarbonSession
import com.karbonpowered.network.MessageHandler
import com.karbonpowered.profile.GameProfile
import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginSuccessPacket
import com.karbonpowered.protocol.packet.serverbound.login.ServerboundLoginStartPacket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object LoginStartHandler : MessageHandler<KarbonSession, ServerboundLoginStartPacket> {
    override fun handle(session: KarbonSession, message: ServerboundLoginStartPacket) {
        GlobalScope.launch {
            val gameProfile = GameProfile(GameProfile.offlineUniqueId(message.username), message.username)
            session.send(
                ClientboundLoginSuccessPacket(
                    gameProfile.uniqueId,
                    message.username
                )
            )
            session.protocol = GameProtocol(true)
            Engine.server.addPlayer(gameProfile, session)
        }
    }
}