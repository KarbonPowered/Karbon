package com.karbonpowered.api.advancement.criteria.trigger

interface FilteredTrigger<C : FilteredTriggerConfiguration> {
    val type: Trigger<C>

    val configuration: C
}