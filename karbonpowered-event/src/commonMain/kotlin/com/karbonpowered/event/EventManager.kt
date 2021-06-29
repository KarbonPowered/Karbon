package com.karbonpowered.event

interface EventManager {
    fun <T : Event> callEvent(event: T): T

    fun registerListener(listener: EventListener)
}