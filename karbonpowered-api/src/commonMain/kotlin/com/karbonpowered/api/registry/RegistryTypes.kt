package com.karbonpowered.api.registry

import com.karbonpowered.api.Identifier
import com.karbonpowered.api.Karbon
import com.karbonpowered.api.block.BlockType
import com.karbonpowered.api.world.biome.Biome
import com.karbonpowered.api.world.chunk.ChunkState
import com.karbonpowered.api.world.height.HeightType
import com.karbonpowered.api.world.palette.PaletteType
import com.karbonpowered.api.world.weather.WeatherType

object RegistryTypes {
    val BIOME = minecraftKey<Biome>("worldgen/biome")
    val BLOCK_TYPE = minecraftKeyInGame<BlockType>("block")
    val CHUNK_STATE = minecraftKeyInGame<ChunkState>("chunk_status")
    val PALETTE_TYPE = karbonKeyInGame<PaletteType<*, *>>("palette_type")
    val HEIGHT_TYPE = karbonKeyInGame<HeightType>("height_type")
    val WEATHER_TYPE = karbonKeyInGame<WeatherType>("weather_type")

    private fun <V> minecraftKey(key: String): RegistryType<V> =
            RegistryType(RegistryRoots.MINECRAFT, Identifier.minecraft(key))

    private fun <V> minecraftKeyInGame(key: String): DefaultedRegistryType<V> =
            RegistryType<V>(RegistryRoots.MINECRAFT, Identifier.minecraft(key)).asDefaultedType { Karbon.game.registries }

    private fun <V> karbonKeyInGame(key: String): DefaultedRegistryType<V> =
            RegistryType<V>(RegistryRoots.KARBON, Identifier.karbon(key)).asDefaultedType { Karbon.game.registries }
}