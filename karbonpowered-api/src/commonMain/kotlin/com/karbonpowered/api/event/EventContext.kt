package com.karbonpowered.api.event

import com.karbonpowered.api.registry.factory

interface EventContext {

    interface Factory {
        fun create(entries: Map<EventContextKey<*>, Any>): EventContext
    }

    companion object {
        fun of(entries: Map<EventContextKey<*>, Any>): EventContext = factory<Factory>().create(entries)
    }
}

inline fun EventContext(entries: Map<EventContextKey<*>, Any>): EventContext = EventContext.of(entries)