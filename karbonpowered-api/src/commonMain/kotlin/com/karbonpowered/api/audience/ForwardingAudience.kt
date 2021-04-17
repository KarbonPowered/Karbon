package com.karbonpowered.api.audience

import com.karbonpowered.common.UUID
import com.karbonpowered.text.Text
import com.karbonpowered.text.TextRepresentable

interface ForwardingAudience : Audience {
    val audiences: Iterable<Audience>

    override fun sendMessage(source: UUID, message: Text, messageType: MessageType) {
        audiences.forEach { it.sendMessage(source, message, messageType) }
    }

    override fun sendActionBar(message: Text) {
        audiences.forEach { it.sendMessage(message) }
    }

    override fun sendActionBar(message: TextRepresentable) {
        audiences.forEach { it.sendMessage(message) }
    }

    override fun sendMessage(message: TextRepresentable) {
        audiences.forEach { it.sendMessage(message) }
    }

    override fun sendMessage(message: TextRepresentable, messageType: MessageType) {
        audiences.forEach { it.sendMessage(message, messageType) }
    }

    override fun sendMessage(source: UUID, message: TextRepresentable) {
        audiences.forEach { it.sendMessage(source, message) }
    }

    override fun sendMessage(source: UUID, message: TextRepresentable, messageType: MessageType) {
        audiences.forEach { it.sendMessage(source, message, messageType) }
    }
}