package com.karbonpowered.api.scoreboard

abstract class Scoreboard(open val name: String) {
    abstract val objectives: List<ScoreboardObjective>
    protected abstract val teams: List<PlayerTeam>

    abstract fun createObjective(name: String): ScoreboardObjective

    abstract fun removeObjective(objective: ScoreboardObjective): Boolean

    abstract fun createTeam(name: String): PlayerTeam

    abstract fun removeTeam(name: String)

    enum class Position {
        LIST,
        SIDEBAR,
        BELOW_NAME
    }
}