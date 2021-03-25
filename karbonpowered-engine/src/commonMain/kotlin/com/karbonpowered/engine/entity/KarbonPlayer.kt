package com.karbonpowered.engine.entity

import com.karbonpowered.api.audience.MessageType
import com.karbonpowered.api.component.Component
import com.karbonpowered.api.entity.EntityType
import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.profile.GameProfile
import com.karbonpowered.api.world.Location
import com.karbonpowered.api.world.World
import com.karbonpowered.common.UUID
import com.karbonpowered.component.BaseComponentOwner
import com.karbonpowered.minecraft.text.Text
import kotlin.reflect.KClass

class KarbonPlayer(
    override val profile: GameProfile,
    override val world: World<*,*>,
    override val location: Location<*, *>
) : BaseComponentOwner(), Player {
    override var health: Double = 20.0
    override val type: EntityType<Player> = EntityType.PLAYER

    override fun onTick(dt: Float) {

    }

    override fun sendMessage(source: UUID, message: Text, messageType: MessageType) {
        println(message)
    }
}