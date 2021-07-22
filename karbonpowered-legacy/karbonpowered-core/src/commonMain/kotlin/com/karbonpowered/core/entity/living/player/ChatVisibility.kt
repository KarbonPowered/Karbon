package com.karbonpowered.core.entity.living.player

import com.karbonpowered.api.entity.living.player.ChatVisibility

internal data class VanillaChatVisibility(
    val name: String
) : ChatVisibility {
    override fun toString(): String = "ChatVisibility($name)"
}

object ChatVisibilities {
    val ENABLED by chatVisibility("enabled")
    val COMMANDS_ONLY by chatVisibility("commands_only")
    val HIDDEN by chatVisibility("hidden")

    private fun chatVisibility(name: String): Lazy<ChatVisibility> = lazy {
        VanillaChatVisibility(name)
    }
}