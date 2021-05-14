package com.karbonpowered.api.advancement.criteria

import com.karbonpowered.api.advancement.criteria.trigger.FilteredTrigger
import com.karbonpowered.common.Named

interface AdvancementCriterion : Named {
    val trigger: FilteredTrigger<*>?

    fun and(vararg criteria: AdvancementCriterion)
    infix fun and(criteria: AdvancementCriterion)
    infix fun and(criteria: Iterable<AdvancementCriterion>)

    fun or(vararg criteria: AdvancementCriterion)
    infix fun or(criteria: AdvancementCriterion)
    infix fun or(criteria: Iterable<AdvancementCriterion>)

    interface CriterionBuilder<T : AdvancementCriterion, B : CriterionBuilder<T, B>> :
        com.karbonpowered.common.builder.Builder<T, B> {
        var trigger: FilteredTrigger<*>
        var name: String
    }

    interface Factory {
        fun empty(): AdvancementCriterion
        fun dummy(): AdvancementCriterion
    }
}