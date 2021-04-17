package com.karbonpowered.api.scoreboard

import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.common.UUID
import com.karbonpowered.minecraft.text.Text

interface ScoreboardObjective {
    val scores: ArrayList<Score>
    val players: List<String>
    val whoCanSee: List<Player>
    val name: String
    var displayName: Text

    fun hasPlayerScore(name: String): Boolean

    fun getPlayerScore(name: String): Score?

    fun removePlayerScore(name: String): Boolean

    fun removePlayerScore(score: Score): Boolean = removePlayerScore(score.name)

    fun setPlayerScore(score: Score): Unit = setPlayerScore(score.name, score.value, score.owner)

    fun setPlayerScore(name: String, value: Int, player: UUID?)
}