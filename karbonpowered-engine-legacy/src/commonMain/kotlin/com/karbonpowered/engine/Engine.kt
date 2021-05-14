package com.karbonpowered.engine

import com.karbonpowered.api.Engine
import com.karbonpowered.api.Game
import com.karbonpowered.api.registry.RegistryHolder
import com.karbonpowered.api.registry.RegistryScope
import com.karbonpowered.api.scoreboard.Scoreboard
import com.karbonpowered.engine.network.KarbonServer
import com.karbonpowered.engine.scoreboard.KarbonScoreboard

object Engine : Engine {
    val scoreboards = mutableListOf<Scoreboard>()
    lateinit var server: KarbonServer
    override lateinit var game: Game

    override fun createScoreboard(name: String): Scoreboard {
        val scoreboard = KarbonScoreboard(name)
        scoreboards.add(scoreboard)
        return scoreboard
    }

    override fun removeScoreboard(scoreboard: Scoreboard) {
        scoreboards.remove(scoreboard)
    }

    override fun removeScoreboard(name: String) {
        val scoreboard = scoreboards.find { it.name == name } ?: return
        scoreboards.remove(scoreboard)
    }

    override lateinit var registryScope: RegistryScope
    override lateinit var registries: RegistryHolder
}