package com.karbonpowered.api.network

import com.karbonpowered.api.profile.GameProfile

interface ServerSideConnection : EngineConnection {
    val profile: GameProfile
}