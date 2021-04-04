package com.karbonpowered.engine.entity

import com.karbonpowered.api.audience.MessageType
import com.karbonpowered.api.entity.EntityType
import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.profile.GameProfile
import com.karbonpowered.common.UUID
import com.karbonpowered.engine.component.KarbonPlayerNetworkComponent
import com.karbonpowered.engine.network.KarbonSession
import com.karbonpowered.engine.world.KarbonWorld
import com.karbonpowered.minecraft.text.Text
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundMessagePacket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KarbonPlayer(
        val session: KarbonSession,
        override val profile: GameProfile,
        override val world: KarbonWorld
) : KarbonHumanoid<Player>(), Player {
    val network = addComponent(KarbonPlayerNetworkComponent(session))

    override val type: EntityType<Player> = EntityType.PLAYER

    override fun sendMessage(source: UUID, message: Text, messageType: MessageType) {
        GlobalScope.launch {
            session.send(ClientboundMessagePacket(message, messageType, source))
        }
    }
}