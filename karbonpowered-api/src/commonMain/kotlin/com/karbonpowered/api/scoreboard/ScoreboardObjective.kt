package com.karbonpowered.api.scoreboard

import com.karbonpowered.api.entity.living.player.Player

interface ScoreboardObjective {
    val scores: ArrayList<Score>
    val teams: Iterable<Team>
    val players: List<String>
    val whoCanSee: List<Player>

    fun hasPlayerScore(name: String): Boolean

    fun getPlayerScore(name: String): Score?

    fun removePlayerScore(name: String)

    fun setPlayerScore(name: String, value: Int)
}