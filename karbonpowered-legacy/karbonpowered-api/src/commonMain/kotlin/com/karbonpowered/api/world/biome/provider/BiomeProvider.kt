package com.karbonpowered.api.world.biome.provider

import com.karbonpowered.api.world.biome.Biome
import com.karbonpowered.api.world.biome.BiomeFinder

interface BiomeProvider : BiomeFinder {
    val choices: Collection<Biome>

    fun withing(x: Int, y: Int, z: Int, size: Int): Set<Biome>
}