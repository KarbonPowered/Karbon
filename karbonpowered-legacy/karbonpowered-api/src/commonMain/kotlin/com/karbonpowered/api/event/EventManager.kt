package com.karbonpowered.api.event

import kotlin.reflect.KClass

interface EventManager {
    fun <T : Event> registerListener(
        plugin: Any,
        eventClass: KClass<T>,
        listener: EventListener<in T>
    )
}