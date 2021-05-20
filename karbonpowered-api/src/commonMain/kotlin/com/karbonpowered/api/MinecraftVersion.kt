package com.karbonpowered.api

interface MinecraftVersion : Comparable<MinecraftVersion> {
    val name: String
    val isLegacy: Boolean
    val protocolVersion: Int

    override fun compareTo(other: MinecraftVersion): Int = protocolVersion.compareTo(other.protocolVersion)
}

