package com.karbonpowered.api.advancement.criteria

import com.karbonpowered.api.Karbon

interface ScoreAdvancementCriterion : AdvancementCriterion {
    val goal: Int

    interface Builder : AdvancementCriterion.CriterionBuilder<ScoreAdvancementCriterion, Builder> {
        var goal: Int
    }

    companion object {
        fun builder() = Karbon.game.builderProvider[Builder::class]

        operator fun invoke(builder: Builder.() -> Unit) = builder().apply(builder).build()
    }
}

fun ScoreAdvancementCriterion(builder: ScoreAdvancementCriterion.Builder.() -> Unit) = ScoreAdvancementCriterion.invoke(builder)