package com.karbonpowered.component.entity

import com.karbonpowered.api.world.WorldLoadOption
import com.karbonpowered.math.Transform

open class NetworkComponent : EntityComponent() {
    override fun canTick(): Boolean = false

    var observer: Boolean = false

    fun finalizeRun(live: Transform) {
        owner.world.chunk(owner.location.chunkPosition, WorldLoadOption.NO_LOAD)
    }
}