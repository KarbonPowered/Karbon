package com.karbonpowered.api.world.gamerule

import com.karbonpowered.api.registry.DefaultedRegistryValue
import kotlin.reflect.KClass

interface GameRule<V> : DefaultedRegistryValue {
    val defaultValue: V

    interface Builder<V> : com.karbonpowered.common.builder.Builder<GameRule<V>, Builder<V>> {
        var name: String
        var valueType: KClass<*>
        var defaultValue: V

        fun <NV : Any> valueType(valueType: KClass<NV>): Builder<NV>
    }
}