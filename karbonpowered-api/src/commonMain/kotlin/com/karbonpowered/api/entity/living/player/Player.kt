package com.karbonpowered.api.entity.living.player

import com.karbonpowered.api.effect.Viewer
import com.karbonpowered.api.entity.living.Humanoid
import com.karbonpowered.api.scoreboard.Score
import com.karbonpowered.api.scoreboard.Scoreboard
import com.karbonpowered.api.scoreboard.ScoreboardObjective

interface Player : Humanoid<Player>, Viewer, User {
    var scoreboard: Scoreboard?

    fun displayScoreboardObjective(scoreboard: Scoreboard, objective: ScoreboardObjective, position: Scoreboard.Position)

    fun hideScoreboardObjective()
}