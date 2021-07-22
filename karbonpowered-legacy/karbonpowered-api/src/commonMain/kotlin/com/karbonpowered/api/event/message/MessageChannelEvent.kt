package com.karbonpowered.api.event.message

import com.karbonpowered.audience.Audience

interface MessageChannelEvent : MessageEvent {
    val originalAudience: Audience

    var audience: Audience?
}