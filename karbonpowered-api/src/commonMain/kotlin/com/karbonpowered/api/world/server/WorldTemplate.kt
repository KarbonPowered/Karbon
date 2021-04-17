package com.karbonpowered.api.world.server

import com.karbonpowered.api.ResourceKeyed
import com.karbonpowered.api.entity.living.player.GameMode
import com.karbonpowered.api.registry.RegistryReference
import com.karbonpowered.api.world.WorldType
import com.karbonpowered.api.world.difficulty.Difficulty
import com.karbonpowered.api.world.generation.ChunkGenerator
import com.karbonpowered.api.world.generation.config.WorldGenerationConfig
import com.karbonpowered.math.vector.IntVector3
import com.karbonpowered.text.Text

interface WorldTemplate : ResourceKeyed {
    val displayName: Text?
    val worldType: RegistryReference<WorldType>
    val generator: ChunkGenerator
    val generationConfig: WorldGenerationConfig
    val gameMode: RegistryReference<GameMode>?
    val difficulty: RegistryReference<Difficulty>?
    val loadOnStartup: Boolean
    val performsSpawnLogin: Boolean
    val hardcore: Boolean?
    val commands: Boolean?
    val pvp: Boolean?
    val viewDistance: Int?
    val spawnPosition: IntVector3?

    companion object
}