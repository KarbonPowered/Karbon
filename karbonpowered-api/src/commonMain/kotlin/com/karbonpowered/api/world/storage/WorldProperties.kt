package com.karbonpowered.api.world.storage

import com.karbonpowered.api.world.difficulty.Difficulty
import com.karbonpowered.api.world.gamerule.GameRuleHolder
import com.karbonpowered.api.world.weather.WeatherUniverse
import com.karbonpowered.math.vector.IntVector3

interface WorldProperties : WeatherUniverse, GameRuleHolder {
    var spawnPosition: IntVector3
    val gameTime: Int
    val dayTime: Int
    val hardcore: Boolean
    val difficulty: Difficulty
}