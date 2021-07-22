package com.karbonpowered.api.world.biome

interface BiomeSampler {
    interface Factory {
        fun columnFuzzed(): BiomeSampler
        fun fuzzy(): BiomeSampler
        fun defaultFinder(): BiomeSampler
    }
}