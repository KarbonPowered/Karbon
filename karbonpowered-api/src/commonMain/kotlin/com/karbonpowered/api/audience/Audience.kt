package com.karbonpowered.api.audience

import com.karbonpowered.common.UUID
import com.karbonpowered.text.Text
import com.karbonpowered.text.TextRepresentable

interface Audience {
    fun sendMessage(source: UUID, message: Text, messageType: MessageType)

    fun sendMessage(message: TextRepresentable) =
            sendMessage(NIL_UUID, message.toText(), MessageTypes.SYSTEM)

    fun sendMessage(source: UUID, message: TextRepresentable) =
            sendMessage(source, message.toText(), MessageTypes.CHAT)

    fun sendMessage(source: UUID, message: TextRepresentable, messageType: MessageType) =
            sendMessage(source, message.toText(), messageType)

    fun sendMessage(message: TextRepresentable, messageType: MessageType) =
            sendMessage(NIL_UUID, message.toText(), messageType)

    fun sendActionBar(message: TextRepresentable) =
            sendMessage(NIL_UUID, message.toText(), MessageTypes.ACTION_BAR)

    fun sendActionBar(message: Text) =
            sendMessage(NIL_UUID, message, MessageTypes.ACTION_BAR)

    companion object {
        val NIL_UUID = UUID(0, 0)
    }
}