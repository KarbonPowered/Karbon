package com.karbonpowered.api.event

import kotlin.reflect.KClass

interface EventManager {
    fun <T : Event> registerListener(
            plugin: Any,
            eventClass: KClass<T>,
            order: Order = Order.DEFAULT,
            listener: EventListener<in T>
    )

    fun <T : Event> registerListener(
            plugin: Any,
            eventClass: KClass<T>,
            listener: EventListener<in T>
    ) = registerListener(plugin, eventClass, Order.DEFAULT, listener)

    fun unregisterListeners(obj: Any)

    fun post(event: Event): Boolean
}