package com.karbonpowered.api.advancement

import com.karbonpowered.data.ResourceKey

interface AdvancementTree {
    val rootAdvancement: Advancement

    val backgroundPath: ResourceKey?
}