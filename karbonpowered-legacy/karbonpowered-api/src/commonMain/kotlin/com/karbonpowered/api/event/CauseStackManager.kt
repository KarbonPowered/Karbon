package com.karbonpowered.api.event


interface CauseStackManager {
    val currentCause: Cause
    val currentContext: EventContext
}