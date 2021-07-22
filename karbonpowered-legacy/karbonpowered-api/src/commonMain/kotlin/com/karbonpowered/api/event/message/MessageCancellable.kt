package com.karbonpowered.api.event.message

interface MessageCancellable : MessageEvent {
    var isMessageCancelled: Boolean
}