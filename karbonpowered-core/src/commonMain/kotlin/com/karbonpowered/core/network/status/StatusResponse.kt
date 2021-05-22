package com.karbonpowered.core.network.status

import com.karbonpowered.api.MinecraftVersion
import com.karbonpowered.api.network.status.Favicon
import com.karbonpowered.api.network.status.StatusResponse
import com.karbonpowered.core.MinecraftVersions
import com.karbonpowered.profile.GameProfile
import com.karbonpowered.text.LiteralText
import com.karbonpowered.text.Text
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StatusResponseImpl(
    override val description: Text,
    override val players: StatusResponse.Players?,
    override val version: MinecraftVersion,
    override val favicon: Favicon?
) : StatusResponse {
    internal data class PlayersImpl(
        override val online: Int,
        override val max: Int,
        @SerialName("sample")
        override val profiles: List<GameProfile>
    ) : StatusResponse.Players {
        data class BuilderImpl(
            override var online: Int = 0,
            override var max: Int = 20,
            override var profiles: MutableList<GameProfile> = ArrayList()
        ) : StatusResponse.Players.Builder {
            override fun build(): StatusResponse.Players =
                PlayersImpl(online, max, profiles)
        }
    }

    data class BuilderImpl(
        override var description: Text = LiteralText("A Minecraft Server"),
        override var players: StatusResponse.Players? = PlayersImpl.BuilderImpl().build(),
        override var version: MinecraftVersion = MinecraftVersions.LATEST_SNAPSHOT,
        override var favicon: Favicon? = null
    ) : StatusResponse.Builder {
        override fun players(builder: StatusResponse.Players.Builder.() -> Unit) {
            players = PlayersImpl.BuilderImpl().apply(builder).build()
        }

        override fun build(): StatusResponse = StatusResponseImpl(description, players, version, favicon)
    }
}

fun StatusResponse(builder: StatusResponse.Builder.() -> Unit): StatusResponse =
    StatusResponseImpl.BuilderImpl().apply(builder).build()
