package com.karbonpowered.api.network

import com.karbonpowered.profile.GameProfile

interface ServerSideConnection : EngineConnection {
    override val side
        get() = EngineConnectionSide.SERVER

    val profile: GameProfile
}