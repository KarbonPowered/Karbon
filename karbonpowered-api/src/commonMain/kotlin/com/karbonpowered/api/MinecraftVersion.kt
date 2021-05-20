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
    val SNAPSHOT_21W20A = snapshot(28, "21w20a")
    val SNAPSHOT_21w19A = snapshot(27, "21w19a")
    val RELEASE_1_16_5 = release(754, "1.16.5")
    val RELEASE_1_16_4 = release(754, "1.16.4")

    val LATEST_RELEASE get() = RELEASE_1_16_5
    val LATEST_SNAPSHOT get() = SNAPSHOT_21W20A

    private fun snapshot(protocol: Int, name: String): MinecraftVersion =
        MinecraftVersionImpl(name, false, 0x40000000 + protocol)

    private fun release(protocol: Int, name: String): MinecraftVersion =
        MinecraftVersionImpl(name, false, protocol)
}