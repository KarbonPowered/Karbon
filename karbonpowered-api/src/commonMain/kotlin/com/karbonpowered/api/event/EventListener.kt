package com.karbonpowered.api.event

fun interface EventListener<T : Event> {
    fun handle(event: T)
}