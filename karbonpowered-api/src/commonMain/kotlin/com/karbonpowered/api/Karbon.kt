package com.karbonpowered.api

import com.karbonpowered.api.scoreboard.Scoreboard

object Karbon {
    lateinit var engine: Engine

    val game: Game get() = engine.game
    private val scoreboards = mutableListOf<Scoreboard>() // хз куда пихать поэтому пока здесь

    fun createScoreboard() {
        TODO("Not implemented")
    }
}