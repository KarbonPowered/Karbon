package com.karbonpowered.event

interface Event {
    fun call(listener: EventListener)
}