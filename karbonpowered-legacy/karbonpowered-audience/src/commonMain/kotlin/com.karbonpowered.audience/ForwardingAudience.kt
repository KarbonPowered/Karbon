package com.karbonpowered.audience

import com.karbonpowered.common.UUID
import com.karbonpowered.text.TextRepresentable

interface ForwardingAudience : Audience {
    val audiences: Iterable<Audience>

    override fun sendMessage(source: UUID, message: TextRepresentable, messageType: MessageType) =
        audiences.forEach { it.sendMessage(source, message, messageType) }

    override fun sendMessage(message: TextRepresentable) =
        audiences.forEach { it.sendMessage(message) }

    override fun sendActionBar(message: TextRepresentable) =
        audiences.forEach { it.sendActionBar(message) }
}