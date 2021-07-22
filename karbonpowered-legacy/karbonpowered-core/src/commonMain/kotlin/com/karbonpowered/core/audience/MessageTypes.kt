package com.karbonpowered.core.audience

import com.karbonpowered.audience.MessageType

internal data class VanillaMessageType(
    val name: String
) : MessageType

object MessageTypes {
    val CHAT by messageType("chat")
    val ACTION_BAR by messageType("action_bar")
    val SYSTEM by messageType("system")

    private fun messageType(name: String): Lazy<MessageType> = lazy {
        VanillaMessageType(name)
    }
}