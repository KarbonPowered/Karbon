package com.karbonpowered.api.event

interface EventContext {
    companion object {
        lateinit var factory: (Map<EventContextKey<*>, Any>) -> EventContext

        operator fun invoke(entries: Map<EventContextKey<*>, Any>): EventContext = factory(entries)
    }
}