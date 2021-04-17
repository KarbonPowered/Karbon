package com.karbonpowered.api.registry

import com.karbonpowered.api.Karbon
import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.advancement.AdvancementType
import com.karbonpowered.api.advancement.criteria.trigger.Trigger
import com.karbonpowered.api.audience.MessageType
import com.karbonpowered.api.block.BlockType
import com.karbonpowered.api.entity.living.player.GameMode
import com.karbonpowered.api.item.ItemType
import com.karbonpowered.api.world.WorldType
import com.karbonpowered.api.world.biome.Biome
import com.karbonpowered.api.world.chunk.ChunkState
import com.karbonpowered.api.world.difficulty.Difficulty
import com.karbonpowered.api.world.height.HeightType
import com.karbonpowered.api.world.palette.PaletteType
import com.karbonpowered.api.world.weather.WeatherType

object RegistryTypes {
    val ADVANCEMENT_TYPE = karbonKeyInGame<AdvancementType>("advancement_type")
    val BIOME = minecraftKey<Biome>("worldgen/biome")
    val BLOCK_TYPE = minecraftKeyInGame<BlockType>("block")
    val CHUNK_STATE = minecraftKeyInGame<ChunkState>("chunk_status")
    val DIFFICULTY = karbonKeyInGame<Difficulty>("difficulty")
    val PALETTE_TYPE = karbonKeyInGame<PaletteType<*, *>>("palette_type")
    val GAME_MODE = karbonKeyInGame<GameMode>("game_mode")
    val MESSAGE_TYPE = karbonKeyInGame<MessageType>("message_type")
    val HEIGHT_TYPE = karbonKeyInGame<HeightType>("height_type")
    val WEATHER_TYPE = karbonKeyInGame<WeatherType>("weather_type")
    val WORLD_TYPE = minecraftKeyInGame<WorldType>("dimension_type")
    val TRIGGER = karbonKeyInGame<Trigger<*>>("trigger")
    val ITEM_TYPE = minecraftKeyInGame<ItemType>("item")

    private fun <V : Any> minecraftKey(key: String): RegistryType<V> =
            RegistryType(RegistryRoots.MINECRAFT, ResourceKey.minecraft(key))

    private fun <V : Any> minecraftKeyInGame(key: String): DefaultedRegistryType<V> =
            RegistryType<V>(RegistryRoots.MINECRAFT, ResourceKey.minecraft(key)).asDefaultedType { Karbon.game.registries }

    private fun <V : Any> karbonKeyInGame(key: String): DefaultedRegistryType<V> =
            RegistryType<V>(RegistryRoots.KARBON, ResourceKey.karbon(key)).asDefaultedType { Karbon.game.registries }
}