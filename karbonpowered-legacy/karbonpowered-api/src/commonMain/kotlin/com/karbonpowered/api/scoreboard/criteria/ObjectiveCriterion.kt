package com.karbonpowered.api.scoreboard.criteria

import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode

interface ObjectiveCriterion : DefaultedRegistryValue {
    val displayMode: ObjectiveDisplayMode
}