package com.karbonpowered.api.advancement.criteria

interface OrCriterion : OperatorCriterion {
    interface Factory {
        fun create(vararg criteria: AdvancementCriterion): AdvancementCriterion
        fun create(criteria: Iterable<AdvancementCriterion>): AdvancementCriterion
    }
}