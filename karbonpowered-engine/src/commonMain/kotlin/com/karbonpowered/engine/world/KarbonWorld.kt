package com.karbonpowered.engine.world

import com.karbonpowered.common.UUID

class KarbonWorld(
    val uniqueId: UUID,
    val name: String
) {
    val regions = RegionSource(this)
}