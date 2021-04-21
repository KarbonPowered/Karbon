package com.karbonpowered.api.world

import com.karbonpowered.api.ResourceKeyed

interface WorldTypeEffect : ResourceKeyed {
    interface Factory {
        fun overworld(): WorldTypeEffect
        fun nether(): WorldTypeEffect
        fun end(): WorldTypeEffect
    }
}