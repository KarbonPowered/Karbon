package com.karbonpowered.api.advancement.criteria

import com.karbonpowered.api.Karbon

interface AndCriterion : OperatorCriterion {
    interface Factory {
        fun create(vararg criteria: AdvancementCriterion): AdvancementCriterion
        fun create(criteria: Iterable<AdvancementCriterion>): AdvancementCriterion
    }

    companion object {
        fun of(vararg criteria: AdvancementCriterion): AdvancementCriterion = Karbon.game.factoryProvider[Factory::class].create(*criteria)
        fun of(criteria: Iterable<AdvancementCriterion>): AdvancementCriterion = Karbon.game.factoryProvider[Factory::class].create(criteria)
    }
}