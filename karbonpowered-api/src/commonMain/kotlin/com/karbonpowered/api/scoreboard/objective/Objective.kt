package com.karbonpowered.api.scoreboard.objective

import com.karbonpowered.api.scoreboard.Score
import com.karbonpowered.api.scoreboard.Scoreboard
import com.karbonpowered.api.scoreboard.criteria.ObjectiveCriterion
import com.karbonpowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode
import com.karbonpowered.text.Text

interface Objective {
    val name: String
    var displayName: Text
    val criterion: ObjectiveCriterion
    var displayMode: ObjectiveDisplayMode
    val scores: Map<Text, Score>
    val scoreboards: Set<Scoreboard>

    fun hasScore(name: Text): Boolean

    fun addScore(score: Score)

    fun score(name: Text): Score? = if (hasScore(name)) scoreOrCreate(name) else null

    fun scoreOrCreate(name: Text): Score

    fun removeScore(score: Score): Boolean

    fun removeScore(name: Text): Boolean

    interface Builder : com.karbonpowered.common.builder.Builder<Objective, Builder> {
        var name: String
        var displayName: Text
        var criterion: ObjectiveCriterion
        var objectiveDisplayMode: ObjectiveDisplayMode
    }
}