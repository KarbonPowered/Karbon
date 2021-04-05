package com.karbonpowered.engine.scoreboard

import com.karbonpowered.api.scoreboard.Team

class KarbonTeam : Team {
    override val players = mutableListOf<String>()
}