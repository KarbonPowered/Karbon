package com.karbonpowered.api.event.world

interface LoadWorldEvent : WorldEvent {
    val hasLoadedBefore: Boolean
}