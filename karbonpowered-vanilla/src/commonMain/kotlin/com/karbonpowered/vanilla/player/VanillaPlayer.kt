package com.karbonpowered.vanilla.player

import com.karbonpowered.audience.MessageType
import com.karbonpowered.common.UUID
import com.karbonpowered.common.uuidOf
import com.karbonpowered.core.audience.MessageTypes
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.player.KarbonPlayer
import com.karbonpowered.engine.player.PlayerNetwork
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundMessagePacket
import com.karbonpowered.server.Session
import com.karbonpowered.text.Text

class VanillaPlayer(
    engine: KarbonEngine,
    uniqueId: UUID,
    username: String,
    session: Session,
) : KarbonPlayer(engine, uniqueId, username, session) {
    override val network: PlayerNetwork = VanillaPlayerNetwork(this, session)

    fun sendMessage(
        message: Text,
        messageType: MessageType = MessageTypes.SYSTEM,
        sender: UUID = uuidOf(ByteArray(16))
    ) = network.session.sendPacket(
        ClientboundMessagePacket(message, messageType, sender)
    )

}