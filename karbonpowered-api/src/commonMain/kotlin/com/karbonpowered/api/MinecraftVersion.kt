package com.karbonpowered.api

interface MinecraftVersion : Comparable<MinecraftVersion> {
    val name: String
    val isLegacy: Boolean
    val protocolVersion: Int

    override fun compareTo(other: MinecraftVersion): Int = protocolVersion.compareTo(other.protocolVersion)
}

internal data class MinecraftVersionImpl(
    override val name: String,
    override val isLegacy: Boolean,
    override val protocolVersion: Int
) : MinecraftVersion

object MinecraftVersions {
    val RELEASE_1_16_5 = version(754, "1.16.5")
    val RELEASE_1_16_4 = version(754, "1.16.4")

    val LATEST_RELEASE get() = RELEASE_1_16_5

    private fun version(protocol: Int, name: String): MinecraftVersion =
        MinecraftVersionImpl(name, false, protocol)
}