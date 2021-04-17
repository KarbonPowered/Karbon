package com.karbonpowered.api.event.message

import com.karbonpowered.api.audience.Audience

interface MessageChannelEvent : MessageEvent {
    val originalAudience: Audience

    var audience: Audience?
}