package com.karbonpowered.component.entity

import com.karbonpowered.api.world.WorldLoadOption
import com.karbonpowered.math.vector.DoubleVector3

open class NetworkComponent : EntityComponent() {
    override fun canTick(): Boolean = false

    var observer: Boolean = false

    fun finalizeRun(position: DoubleVector3, rotation: DoubleVector3) {
        owner.world.chunk(owner.location.chunkPosition, WorldLoadOption.NO_LOAD)
    }
}