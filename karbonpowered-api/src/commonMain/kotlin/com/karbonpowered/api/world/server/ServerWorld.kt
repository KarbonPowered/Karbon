package com.karbonpowered.api.world.server

import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.entity.Entity
import com.karbonpowered.api.entity.living.player.server.ServerPlayer
import com.karbonpowered.api.world.World
import com.karbonpowered.api.world.weather.WeatherUniverse
import com.karbonpowered.common.Identifiable

interface ServerWorld : World<ServerWorld, ServerLocation>,
    Identifiable,
    ServerLocationCreator,
    WeatherUniverse.Mutable {
    override val players: Collection<ServerPlayer>
    val entities: Collection<Entity<*>>

    val key: ResourceKey
}