package com.karbonpowered.api.event.message

import com.karbonpowered.api.event.Event
import com.karbonpowered.text.Text

interface MessageEvent : Event {
    val originalMessage: Text
    var message: Text
}