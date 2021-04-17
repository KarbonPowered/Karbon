package com.karbonpowered.api.world.generation.config.structure

interface StructureGenerationConfig {
    val stronghold: SpacedStructureConfig?
    val structures: Map<Structure, SeparatedStructureConfig>

    fun structure(structure: Structure): SeparatedStructureConfig?
}