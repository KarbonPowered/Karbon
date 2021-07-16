package com.karbonpowered.vanilla.handler

import com.karbonpowered.core.audience.MessageTypes
import com.karbonpowered.protocol.packet.serverbound.game.ServerboundChatPacket
import com.karbonpowered.server.event.PacketReceivedEvent
import com.karbonpowered.server.event.SessionListener
import com.karbonpowered.text.LiteralText
import com.karbonpowered.vanilla.VanillaServer
import com.karbonpowered.vanilla.player.VanillaPlayer

class ChatHandler(
    val server: VanillaServer,
    val player: VanillaPlayer
) : SessionListener {
    override fun packetReceived(event: PacketReceivedEvent) {
        val packet = event.packet
        if (packet is ServerboundChatPacket) {
            val message = packet.message
            server.players.forEach { (_, otherPlayer) ->
                otherPlayer.sendMessage(
                    LiteralText("<${player.username}> $message"),
                    MessageTypes.CHAT,
                    player.uniqueId
                )
            }
        }
    }
}