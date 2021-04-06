package com.karbonpowered.api.scoreboard

abstract class Scoreboard {
    abstract val objectives: List<ScoreboardObjective>
    protected abstract val teams: List<PlayerTeam>

    abstract fun createObjective(name: String): ScoreboardObjective

    abstract fun removeObjective(objective: ScoreboardObjective): Boolean

    abstract fun createTeam(name: String): PlayerTeam

    abstract fun removeTeam(name: String)
}