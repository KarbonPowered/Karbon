package com.karbonpowered.api.world.generation

import com.karbonpowered.api.world.biome.provider.BiomeProvider
import com.karbonpowered.api.world.generation.config.structure.StructureGenerationConfig

interface ChunkGenerator {
    val biomeProvider: BiomeProvider

    val structureConfig: StructureGenerationConfig
}