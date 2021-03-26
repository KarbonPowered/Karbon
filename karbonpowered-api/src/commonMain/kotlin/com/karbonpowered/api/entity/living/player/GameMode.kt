package com.karbonpowered.api.entity.living.player

import com.karbonpowered.common.Named

interface GameMode : Named {
    override val name: String
}