package com.karbonpowered.api.world

import com.karbonpowered.api.ResourceKeyed

interface WorldTypeEffect : ResourceKeyed {
    interface Factory {
        fun overworld(): WorldTypeEffect
        fun nether(): WorldTypeEffect
        fun end(): WorldTypeEffect
    }

    companion object {
        lateinit var factory: Factory
    }
}

object WorldTypeEffects {
    val OVERWORLD = WorldTypeEffect.factory.overworld()
    val NETHER = WorldTypeEffect.factory.nether()
    val END = WorldTypeEffect.factory.end()
}