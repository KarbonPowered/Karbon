package com.karbonpowered.engine.network.handler

import com.karbonpowered.api.audience.MessageTypes
import com.karbonpowered.engine.Engine
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.logging.Logger
import com.karbonpowered.network.MessageHandler
import com.karbonpowered.network.Session
import com.karbonpowered.protocol.packet.serverbound.game.ServerboundChatPacket
import com.karbonpowered.text.LiteralText

object ChatHandler : MessageHandler<Session, ServerboundChatPacket> {
    override fun handle(session: Session, message: ServerboundChatPacket) {
        val sender = Engine.server.players.find { it as KarbonPlayer; it.session == session } ?: return
        val chatMessage = "<${sender.profile.name}> ${message.message}"
        Logger.info("[Chat] $chatMessage")
        Engine.server.players.forEach {
            it.sendMessage(
                    sender.profile.uniqueId,
                    LiteralText(chatMessage),
                    MessageTypes.CHAT
            )
        }
    }
}