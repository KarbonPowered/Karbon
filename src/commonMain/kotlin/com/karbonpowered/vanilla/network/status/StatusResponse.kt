package com.karbonpowered.vanilla.network.status

import kotlinx.serialization.Serializable

@Serializable
data class StatusResponse(
    val version: Version = Version(),
    val players: Players = Players(),
    val description: String = "A KarbonPowered server",
    val favicon: String? = null
) {
    @Serializable
    data class Version(
        val name: String = "1.17.1",
        val protocol: Int = 756
    )

    @Serializable
    data class Players(
        val max: Int = 20,
        val online: Int = 0,
        val sample: List<Profile> = emptyList()
    ) {
        @Serializable
        data class Profile(
            val name: String,
            val id: String
        )
    }
}