package com.karbonpowered.api.entity.living.player.server

import com.karbonpowered.api.entity.living.player.Player

interface ServerPlayer : Player {
    var isOnline: Boolean
}