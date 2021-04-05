package com.karbonpowered.engine.scoreboard

import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.scoreboard.Score
import com.karbonpowered.api.scoreboard.ScoreboardObjective
import com.karbonpowered.api.scoreboard.Team

class KarbonObjective : ScoreboardObjective {
    override val scores = arrayListOf<Score>()
    override val teams = mutableListOf<Team>()
    override val whoCanSee = mutableListOf<Player>()
    override val players: List<String>
        get() = scores.map { it.name }

    override fun hasPlayerScore(name: String): Boolean {
        return players.contains(name)
    }

    override fun getPlayerScore(name: String): Score? {
        return scores.find { it.name == name }
    }

    override fun removePlayerScore(name: String) {
        TODO("Not yet implemented")
    }

    override fun setPlayerScore(name: String, value: Int) {
        TODO("Not yet implemented")
    }
}