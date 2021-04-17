package com.karbonpowered.api.world.server.storage

import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.entity.living.player.GameMode
import com.karbonpowered.api.world.WorldBorder
import com.karbonpowered.api.world.WorldType
import com.karbonpowered.api.world.difficulty.Difficulty
import com.karbonpowered.api.world.generation.config.WorldGenerationConfig
import com.karbonpowered.api.world.server.ServerWorld
import com.karbonpowered.api.world.storage.WorldProperties
import com.karbonpowered.api.world.weather.WeatherUniverse
import com.karbonpowered.common.Identifiable
import com.karbonpowered.common.UUID
import com.karbonpowered.text.Text

interface ServerWorldProperties : WorldProperties, Identifiable, ResourceKey, WeatherUniverse.Mutable {
    val world: ServerWorld?
    var displayName: Text?
    val isInitialized: Boolean
    var loadOnStartup: Boolean
    var performsSpawnLogic: Boolean
    var worldGenerationConfig: WorldGenerationConfig
    override var dayTime: Int
    var worldType: WorldType
    var pvp: Boolean
    var gameMode: GameMode
    override var hardcore: Boolean
    var commands: Boolean
    override var difficulty: Difficulty
    var wanderingTraderSpawnDelay: Int
    var wanderTraderUniqueId: UUID?
    var viewDistance: Int
    val worldBorder: WorldBorder
}