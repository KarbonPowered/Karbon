package com.karbonpowered.engine.scoreboard

import com.karbonpowered.api.scoreboard.PlayerTeam
import com.karbonpowered.api.scoreboard.Scoreboard
import com.karbonpowered.api.scoreboard.ScoreboardObjective

class KarbonScoreboard(override val name: String) : Scoreboard(name) {
    init {
        if (name.length > 16) {
            throw IllegalArgumentException("Name length cannot be more than 16")
        }
    }

    override val objectives = mutableListOf<KarbonObjective>()
    override val teams = mutableListOf<KarbonTeam>()

    override fun createObjective(name: String): ScoreboardObjective {
        val objective = KarbonObjective(name)
        objectives.add(objective)
        return objective
    }

    override fun removeObjective(objective: ScoreboardObjective) = objectives.remove(objective)

    override fun createTeam(name: String): PlayerTeam {
        val team = KarbonTeam(name)
        teams.add(team)
        team.sendCreatePacket()
        return team
    }

    override fun removeTeam(name: String) {
        val team = teams.find { it.name == name } ?: return
        teams.remove(team)
        team.sendRemovePacket()
    }
}