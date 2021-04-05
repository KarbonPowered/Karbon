package com.karbonpowered.api.scoreboard

interface Scoreboard {
    val objectives: List<ScoreboardObjective>

    fun createObjective(): ScoreboardObjective

    fun removeObjective(scoreboardObjective: ScoreboardObjective): Boolean
}