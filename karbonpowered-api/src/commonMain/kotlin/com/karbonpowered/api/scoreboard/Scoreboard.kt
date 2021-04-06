package com.karbonpowered.api.scoreboard

interface Scoreboard {
    val objectives: List<ScoreboardObjective>
    val teams: List<PlayerTeam>

    fun createObjective(name: String): ScoreboardObjective

    fun removeObjective(objective: ScoreboardObjective): Boolean

    fun createTeam(name: String): Team

    fun removeTeam(name: String)
}