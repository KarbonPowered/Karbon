package com.karbonpowered.api.advancement.criteria

interface ScoreAdvancementCriterion : AdvancementCriterion {
    val goal: Int

    interface Builder : AdvancementCriterion.CriterionBuilder<ScoreAdvancementCriterion, Builder> {
        var goal: Int
    }
}