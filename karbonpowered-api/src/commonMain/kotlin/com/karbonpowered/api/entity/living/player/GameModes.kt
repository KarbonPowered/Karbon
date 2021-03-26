package com.karbonpowered.api.entity.living.player

object GameModes {
    val SURVIVAL = object : GameMode {
        override val name: String = "survival"
    }
    val CREATIVE = object : GameMode {
        override val name: String = "creative"
    }
    val ADVENTURE = object : GameMode {
        override val name: String = "adventure"
    }
    val SPECTATOR = object : GameMode {
        override val name: String = "adventure"
    }
}