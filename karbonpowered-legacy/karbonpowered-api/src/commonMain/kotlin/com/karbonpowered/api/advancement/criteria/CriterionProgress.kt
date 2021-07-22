package com.karbonpowered.api.advancement.criteria

import com.karbonpowered.api.advancement.Progressable

interface CriterionProgress : Progressable {
    val criterion: AdvancementCriterion
}