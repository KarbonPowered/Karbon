package com.karbonpowered.audience

import com.karbonpowered.common.UUID
import com.karbonpowered.text.TextRepresentable

interface Audience {
    fun sendMessage(source: UUID, message: TextRepresentable, messageType: MessageType)

    fun sendMessage(message: TextRepresentable)

    fun sendActionBar(message: TextRepresentable)
}