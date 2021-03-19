package com.karbonpowered.minecraft.api.network

import com.karbonpowered.minecraft.api.profile.GameProfile

interface ServerSideConnection : EngineConnection {
    val profile: GameProfile
}