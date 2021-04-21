package com.karbonpowered.api.advancement.criteria.trigger

import com.karbonpowered.api.ResourceKeyed
import com.karbonpowered.api.entity.living.player.server.ServerPlayer
import com.karbonpowered.api.registry.DefaultedRegistryValue
import kotlin.reflect.KClass

interface Trigger<C : FilteredTriggerConfiguration> : DefaultedRegistryValue, ResourceKeyed {
    val configurationType: KClass<*>

    fun trigger()
    fun trigger(player: ServerPlayer)
    fun trigger(vararg player: ServerPlayer)
    fun trigger(player: Iterable<ServerPlayer>)
}