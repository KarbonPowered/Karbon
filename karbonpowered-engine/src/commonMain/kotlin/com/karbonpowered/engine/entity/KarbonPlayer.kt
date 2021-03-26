package com.karbonpowered.engine.entity

import com.karbonpowered.api.audience.MessageType
import com.karbonpowered.api.entity.EntityType
import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.profile.GameProfile
import com.karbonpowered.api.world.Location
import com.karbonpowered.api.world.World
import com.karbonpowered.common.UUID
import com.karbonpowered.minecraft.text.Text

class KarbonPlayer(
    override val profile: GameProfile,
    override val location: Location<*, *>
) : KarbonHumanoid<Player>(), Player {
    override val world: World<*, *>
        get() = requireNotNull(location.world)
    override val type: EntityType<Player> = EntityType.PLAYER

    override fun onTick(dt: Float) {

    }

    override fun sendMessage(source: UUID, message: Text, messageType: MessageType) {
        println(message)
    }
}