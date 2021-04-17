package com.karbonpowered.api.network

import com.karbonpowered.api.entity.living.player.Player

interface PlayerConnection : EngineConnection {
    val player: Player
}