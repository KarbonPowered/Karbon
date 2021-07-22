package com.karbonpowered.api.event

interface Cancellable : Event {
    var cancelled: Boolean
}