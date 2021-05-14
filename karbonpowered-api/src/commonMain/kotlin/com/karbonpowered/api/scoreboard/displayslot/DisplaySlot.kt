package com.karbonpowered.api.scoreboard.displayslot

import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.text.NamedTextColor

interface DisplaySlot : DefaultedRegistryValue {
    val teamColor: NamedTextColor?

    fun withTeamColor(color: NamedTextColor?): DisplaySlot
}