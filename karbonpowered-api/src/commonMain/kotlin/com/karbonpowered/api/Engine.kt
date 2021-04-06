package com.karbonpowered.api

import com.karbonpowered.api.registry.ScopedRegistryHolder
import com.karbonpowered.api.scoreboard.Scoreboard

interface Engine : ScopedRegistryHolder {
    val game: Game

    fun createScoreboard(name: String): Scoreboard

    fun removeScoreboard(scoreboard: Scoreboard)

    fun removeScoreboard(name: String)
}