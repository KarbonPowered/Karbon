package com.karbonpowered.component.entity

import com.karbonpowered.api.world.WorldLoadOption
import com.karbonpowered.math.vector.DoubleVector3
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

open class NetworkComponent : EntityComponent() {
    override fun canTick(): Boolean = false

    var observer: Boolean = false

    fun finalizeRun(position: DoubleVector3, rotation: DoubleVector3) {
        owner.world.chunk(owner.location.chunkPosition, WorldLoadOption.NO_LOAD)
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun tick(duration: Duration) {
        if (canTick()) {
            onTick(duration)
        }
    }
}