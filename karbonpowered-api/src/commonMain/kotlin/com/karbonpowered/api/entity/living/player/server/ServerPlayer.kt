package com.karbonpowered.minecraft.api.entity.living.player.server

import com.karbonpowered.minecraft.api.entity.living.player.Player

interface ServerPlayer : Player {
    var isOnline: Boolean
}