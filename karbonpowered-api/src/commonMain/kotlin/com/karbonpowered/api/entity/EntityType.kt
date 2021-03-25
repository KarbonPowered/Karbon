package com.karbonpowered.api.entity

import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.registry.DefaultedRegistryValue

interface EntityType<A : Entity<A>> : DefaultedRegistryValue {

    companion object {
        val PLAYER = object : EntityType<Player> {
        }
    }
}