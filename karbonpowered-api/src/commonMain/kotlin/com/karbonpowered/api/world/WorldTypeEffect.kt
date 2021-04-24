package com.karbonpowered.api.world

import com.karbonpowered.data.ResourceKeyed

interface WorldTypeEffect : ResourceKeyed {
    interface Factory {
        fun overworld(): WorldTypeEffect
        fun nether(): WorldTypeEffect
        fun end(): WorldTypeEffect
    }
}