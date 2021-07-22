package com.karbonpowered.api.event

import com.karbonpowered.api.util.ResourceKeyedBuilder
import com.karbonpowered.data.ResourceKeyed
import kotlin.reflect.KClass

interface EventContextKey<T> : ResourceKeyed {

    interface Builder<T : Any> : ResourceKeyedBuilder<EventContextKey<T>, Builder<T>> {
        var type: KClass<*>

        fun <N : Any> type(type: KClass<N>): Builder<N>
    }
}