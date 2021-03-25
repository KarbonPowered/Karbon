package com.karbonpowered.api.audience

import com.karbonpowered.common.UUID
import com.karbonpowered.minecraft.text.Text
import com.karbonpowered.minecraft.text.TextRepresentable

interface Audience {
    fun sendMessage(source: UUID, message: Text, messageType: MessageType)

    fun sendMessage(message: TextRepresentable) =
        sendMessage(NIL_UUID, message.toText(), MessageType.SYSTEM)

    fun sendMessage(source: UUID, message: TextRepresentable) =
        sendMessage(source, message.toText(), MessageType.CHAT)

    fun sendMessage(source: UUID, message: TextRepresentable, messageType: MessageType) =
        sendMessage(source, message.toText(), messageType)

    fun sendMessage(message: TextRepresentable, messageType: MessageType) =
        sendMessage(NIL_UUID, message.toText(), messageType)

    fun sendActionBar(message: TextRepresentable) =
        sendMessage(NIL_UUID, message.toText(), MessageType.ACTION_BAR)

    fun sendActionBar(message: Text) =
        sendMessage(NIL_UUID, message, MessageType.ACTION_BAR)

    companion object {
        val NIL_UUID = UUID(0, 0)
    }
}