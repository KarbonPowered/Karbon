package com.karbonpowered.core.entity.living.player

import com.karbonpowered.api.entity.living.player.GameMode
import com.karbonpowered.text.LiteralText
import com.karbonpowered.text.Text

internal data class VanillaGameMode(val name: String) : GameMode {
    override fun toText(): Text = LiteralText(name)

    override fun toString(): String = "GameMode($name)"
}

object GameModes {
    val SURVIVAL by gameMode("survival")
    val CREATIVE by gameMode("creative")
    val ADVENTURE by gameMode("adventure")
    val SPECTATOR by gameMode("spectator")
    val NOT_SET by gameMode("not_set")

    private fun gameMode(name: String): Lazy<GameMode> = lazy { VanillaGameMode(name) }
}