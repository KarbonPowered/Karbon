package com.karbonpowered.api.entity.living.player

import com.karbonpowered.api.effect.Viewer
import com.karbonpowered.api.entity.living.Humanoid
import com.karbonpowered.profile.GameProfile

interface Player : Humanoid<Player>, Viewer, User {
    val profile: GameProfile
}