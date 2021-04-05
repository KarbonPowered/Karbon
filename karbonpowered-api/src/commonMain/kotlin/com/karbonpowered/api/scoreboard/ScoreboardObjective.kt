package com.karbonpowered.api.scoreboard

interface ScoreboardObjective {
    val scores: ArrayList<Score>

    fun hasPlayerScore(name: String)

    fun getPlayerScore(name: String)

    fun removePlayerScore(name: String)

    fun setPlayerScore(name: String, value: Int)

    fun getPlayers(): ArrayList<String>

}