package com.karbonpowered.api.entity.living.player

import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.text.LiteralText
import com.karbonpowered.text.Text
import com.karbonpowered.text.TextRepresentable

sealed interface GameMode : DefaultedRegistryValue, TextRepresentable {
    object Survival : GameMode {
        override fun toText(): Text = LiteralText("survival")
    }

    object Creative : GameMode {
        override fun toText(): Text = LiteralText("creative")
    }

    object Adventure : GameMode {
        override fun toText(): Text = LiteralText("adventure")
    }

    object Spectator : GameMode {
        override fun toText(): Text = LiteralText("spectator")
    }

    object NotSet : GameMode {
        override fun toText(): Text = LiteralText("not_set")
    }
}

object GameModes {
    val SURVIVAL = GameMode.Survival
    val CREATIVE = GameMode.Creative
    val ADVENTURE = GameMode.Adventure
    val SPECTATOR = GameMode.Spectator
    val NOT_SET = GameMode.NotSet
}