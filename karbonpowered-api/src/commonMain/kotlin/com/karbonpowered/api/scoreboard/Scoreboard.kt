package com.karbonpowered.api.scoreboard

import com.karbonpowered.api.scoreboard.criteria.ObjectiveCriterion
import com.karbonpowered.api.scoreboard.displayslot.DisplaySlot
import com.karbonpowered.api.scoreboard.objective.Objective
import com.karbonpowered.common.builder.Builder
import com.karbonpowered.text.Text

interface Scoreboard {
    val objectives: Set<Objective>
    val scores: Set<Score>

    fun objective(name: String): Objective?

    fun objective(slot: DisplaySlot): Objective?

    fun addObjective(objective: Objective)

    fun updateDisplaySlot(objective: Objective?, displaySlot: DisplaySlot)

    fun clearSlot(slot: DisplaySlot) = updateDisplaySlot(null, slot)

    fun objectivesByCriterion(criterion: ObjectiveCriterion): Set<Objective>

    fun removeObjective(objective: Objective)

    fun scores(name: Text): Set<Score>

    fun removeScores(name: Text)

    interface Builder : com.karbonpowered.common.builder.Builder<Scoreboard, Builder> {
        var objectives: MutableList<Objective>
        var teams: MutableList<Team>
    }
}