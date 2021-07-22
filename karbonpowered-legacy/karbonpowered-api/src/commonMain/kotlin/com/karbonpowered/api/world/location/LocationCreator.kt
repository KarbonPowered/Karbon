package com.karbonpowered.api.world.location

import com.karbonpowered.api.world.World

interface LocationCreator<W : World<W, L>, L : Location<W, L>> {
    val world: W
}