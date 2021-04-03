package com.karbonpowered.engine.entity

import com.karbonpowered.api.audience.MessageType
import com.karbonpowered.api.entity.EntityType
import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.profile.GameProfile
import com.karbonpowered.api.world.Location
import com.karbonpowered.api.world.World
import com.karbonpowered.common.UUID
import com.karbonpowered.engine.network.KarbonSession
import com.karbonpowered.minecraft.text.Text
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundMessagePacket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KarbonPlayer(
    val session: KarbonSession,
    override val profile: GameProfile,
    override val location: Location<*, *>
) : KarbonHumanoid<Player>(), Player {
    override val world: World<*, *>
        get() = requireNotNull(location.world)

    override val type: EntityType<Player> = EntityType.PLAYER

    override fun sendMessage(source: UUID, message: Text, messageType: MessageType) {
        GlobalScope.launch {
            session.send(ClientboundMessagePacket(message, messageType, source))
        }
    }
}