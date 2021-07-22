package com.karbonpowered.api.event

interface EventListener<T : Event> {
    fun handle(event: T)
}