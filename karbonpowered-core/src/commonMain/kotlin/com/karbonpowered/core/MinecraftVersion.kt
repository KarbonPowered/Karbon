package com.karbonpowered.core

import com.karbonpowered.api.MinecraftVersion
import kotlinx.serialization.Serializable

@Serializable
internal data class MinecraftVersionImpl(
    override val name: String,
    override val protocol: Int
) : MinecraftVersion

object MinecraftVersions {
    val RELEASE_1_17 by release(755, "1.17")
    val SNAPSHOT_21W20A by snapshot(28, "21w20a")
    val SNAPSHOT_21w19A by snapshot(27, "21w19a")
    val RELEASE_1_16_5 by release(754, "1.16.5")
    val RELEASE_1_16_4 by release(754, "1.16.4")

    val LATEST_RELEASE get() = RELEASE_1_17
    val LATEST_SNAPSHOT get() = SNAPSHOT_21W20A

    private fun snapshot(protocol: Int, name: String): Lazy<MinecraftVersion> =
        lazy { MinecraftVersionImpl(name, 0x40000000 + protocol) }

    private fun release(protocol: Int, name: String): Lazy<MinecraftVersion> =
        lazy { MinecraftVersionImpl(name, protocol) }
}