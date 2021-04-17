package com.karbonpowered.api.event

interface Event {
    val cause: Cause
    val source: Any get() = cause.root
    val context: EventContext get() = cause.context
}