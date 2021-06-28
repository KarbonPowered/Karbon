package com.karbonpowered.vanilla.world

import com.karbonpowered.data.ResourceKey
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.world.KarbonWorld
import com.karbonpowered.engine.world.generator.WorldGenerator
import com.karbonpowered.nbt.NBT

class VanillaWorld(
    engine: KarbonEngine, identifier: ResourceKey, generator: WorldGenerator
) : KarbonWorld(engine, identifier, generator) {
    fun createDimensionCodec() = NBT(
        "minecraft:dimension_type" to NBT(
            "type" to "minecraft:dimension_type",
            "value" to listOf(
                NBT(
                    "name" to "minecraft:overworld",
                    "id" to 0,
                    "element" to createOverworldTag()
                )
            )
        ),
        "worldgen/biome" to NBT(
            "type" to "minecraft:worldgen/biome",
            "value" to listOf(
                createBiome("plains", 0)
            )
        )
    )

    fun createOverworldTag() = NBT(
        "piglin_safe" to false,
        "natural" to true,
        "ambient_light" to 1.0f,
        "infiniburn" to "minecraft:infiniburn_overworld",
        "respawn_anchor_works" to false,
        "has_skylight" to true,
        "bed_works" to true,
        "effects" to "minecraft:overworld",
        "has_raids" to true,
        "height" to 384,
        "min_y" to -64,
        "logical_height" to 384,
        "coordinate_scale" to 1.0,
        "ultrawarm" to false,
        "has_ceiling" to false
    )

    private fun createBiome(name: String, id: Int) = NBT(
        "name" to "minecraft:$name",
        "id" to id,
        "element" to NBT(
            "precipitation" to "rain",
            "effects" to createBiomeEffectTag(),
            "depth" to 0.125F,
            "temperature" to 0.8,
            "scale" to 0.05f,
            "downfall" to 0.4F,
            "category" to "plains"
        )
    )

    private fun createBiomeEffectTag() = NBT(
        "sky_color" to 7907327,
        "water_fog_color" to 329011,
        "fog_color" to 12638463,
        "water_color" to 4159204,
        "mood_sound" to NBT(
            "tick_delay" to 6000,
            "offset" to 2.0,
            "sound" to "minecraft:ambient.cave",
            "block_search_extent" to 8
        )
    )
}