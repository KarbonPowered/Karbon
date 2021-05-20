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

        interface Builder : com.karbonpowered.common.builder.Builder<Players, Builder> {
            var online: Int
            var max: Int
            var profiles: MutableList<GameProfile>
        }
    }

    interface Builder : com.karbonpowered.common.builder.Builder<StatusResponse, Builder> {
        var description: Text
        var players: Players?
        var version: MinecraftVersion
        var favicon: Favicon?

        fun players(builder: Players.Builder.() -> Unit)
    }
}