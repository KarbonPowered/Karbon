package com.karbonpowered.api.scoreboard

import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.profile.GameProfile

abstract class PlayerTeam : Team {
    protected abstract val players: List<GameProfile>

    abstract fun addPlayer(player: Player): Boolean

    abstract fun addPlayer(gameProfile: GameProfile): Boolean

    abstract fun removePlayer(player: Player): Boolean

    abstract fun removePlayer(gameProfile: GameProfile): Boolean
}