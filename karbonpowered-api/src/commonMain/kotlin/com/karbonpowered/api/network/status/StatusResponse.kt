package com.karbonpowered.api.network.status

import com.karbonpowered.api.MinecraftVersion
import com.karbonpowered.profile.GameProfile
import com.karbonpowered.text.Text

interface StatusResponse {
    val description: Text
    val players: Players?
    val version: MinecraftVersion
    val favicon: Favicon?

    interface Players {
        val online: Int
        val max: Int
        val profiles: List<GameProfile>
    }
}