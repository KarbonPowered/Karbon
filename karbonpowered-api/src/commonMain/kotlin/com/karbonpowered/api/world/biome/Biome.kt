package com.karbonpowered.api.world.biome

/**
 * Represents a biome.
 */
interface Biome {
    /**
     * Represents the temperature of this biome.
     */
    val temperature: Double

    /**
     * Represents the humidity of this biome.
     */
    val humidity: Double
}