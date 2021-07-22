package com.karbonpowered.api.advancement.criteria

interface OperatorCriterion : AdvancementCriterion {
    val criteria: Collection<AdvancementCriterion>
    val leafCriteria: Collection<AdvancementCriterion>

    fun find(name: String): Collection<AdvancementCriterion>
    fun findFirst(name: String): AdvancementCriterion?
}