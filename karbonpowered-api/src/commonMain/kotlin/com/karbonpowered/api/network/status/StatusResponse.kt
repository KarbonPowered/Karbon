package com.karbonpowered.api.network.status

import com.karbonpowered.api.MinecraftVersion
import com.karbonpowered.api.MinecraftVersions
import com.karbonpowered.profile.GameProfile
import com.karbonpowered.text.LiteralText
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

        companion object {
            var builder: () -> Builder = { StatusResponseImpl.PlayersImpl.BuilderImpl() }
        }
    }

    interface Builder : com.karbonpowered.common.builder.Builder<StatusResponse, Builder> {
        var description: Text
        var players: Players?
        var version: MinecraftVersion
        var favicon: Favicon?

        fun players(builder: Players.Builder.()->Unit)
    }

    companion object {
        var builder: () -> Builder = { StatusResponseImpl.BuilderImpl() }
    }
}

fun StatusResponse(builder: StatusResponse.Builder.() -> Unit): StatusResponse =
    StatusResponse.builder().apply(builder).build()

data class StatusResponseImpl(
    override val description: Text,
    override val players: StatusResponse.Players?,
    override val version: MinecraftVersion,
    override val favicon: Favicon?
) : StatusResponse {
    data class PlayersImpl(
        override val online: Int,
        override val max: Int,
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
        override var players: StatusResponse.Players? = StatusResponse.Players.builder().build(),
        override var version: MinecraftVersion = MinecraftVersions.LATEST_RELEASE,
        override var favicon: Favicon? = null
    ) : StatusResponse.Builder {
        override fun players(builder: StatusResponse.Players.Builder.() -> Unit) {
            players = StatusResponse.Players.builder().apply(builder).build()
        }

        override fun build(): StatusResponse = StatusResponseImpl(description, players, version, favicon)
    }
}
