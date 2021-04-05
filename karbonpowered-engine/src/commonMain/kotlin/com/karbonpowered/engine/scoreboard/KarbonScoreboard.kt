package com.karbonpowered.engine.scoreboard

import com.karbonpowered.api.scoreboard.Scoreboard
import com.karbonpowered.api.scoreboard.ScoreboardObjective

class KarbonScoreboard : Scoreboard {
    override val objectives = mutableListOf<KarbonObjective>()

    override fun createObjective(): ScoreboardObjective {
        val objective = KarbonObjective()
        objectives.add(objective)
        return objective
    }

    override fun removeObjective(objective: ScoreboardObjective) = objectives.remove(objective)
}