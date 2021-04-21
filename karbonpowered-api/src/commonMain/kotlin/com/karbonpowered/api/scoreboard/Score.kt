package com.karbonpowered.api.scoreboard

import com.karbonpowered.api.scoreboard.objective.Objective
import com.karbonpowered.text.Text

interface Score {
    val name: Text
    var score: Int
    var isLocked: Boolean
    val objectives: Set<Objective>
}