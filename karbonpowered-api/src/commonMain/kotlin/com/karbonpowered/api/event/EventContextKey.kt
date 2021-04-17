package com.karbonpowered.api.event

import com.karbonpowered.api.Karbon
import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.ResourceKeyed
import com.karbonpowered.api.util.ResourceKeyedBuilder
import kotlin.reflect.KClass

interface EventContextKey<T> : ResourceKeyed {

    interface Builder<T : Any> : ResourceKeyedBuilder<EventContextKey<T>, Builder<T>> {
        var type: KClass<*>

        fun <N : Any> type(type: KClass<N>): Builder<N>
    }

    companion object {
        fun builder(): Builder<*> = Karbon.game.builderProvider[Builder::class as KClass<Builder<Any>>]
    }
}

inline fun <reified T : Any> EventContextKey(location: ResourceKey) =
        EventContextKey.builder().type(T::class).key(location).build()

object EventContextKeys {

}