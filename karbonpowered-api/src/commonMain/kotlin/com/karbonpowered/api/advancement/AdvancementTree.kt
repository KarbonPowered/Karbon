package com.karbonpowered.api.advancement

import com.karbonpowered.api.ResourceKey

interface AdvancementTree {
    val rootAdvancement: Advancement

    val backgroundPath: ResourceKey?
}