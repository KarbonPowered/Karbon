package com.karbonpowered.api.world.biome

interface BiomeFinder {
    fun find(x: Int, y: Int, z: Int): Biome
}