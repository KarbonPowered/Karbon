package com.karbonpowered.api.world.gamerule

interface GameRuleHolder {
    val gameRules: Map<GameRule<*>, *>

    fun <V> gameRule(gameRule: GameRule<V>): V

    fun <V> setGameRule(gameRule: GameRule<V>, value: V)
}

operator fun <V> GameRuleHolder.get(gameRule: GameRule<V>): V = gameRule(gameRule)
operator fun <V> GameRuleHolder.set(gameRule: GameRule<V>, value: V) = setGameRule(gameRule, value)