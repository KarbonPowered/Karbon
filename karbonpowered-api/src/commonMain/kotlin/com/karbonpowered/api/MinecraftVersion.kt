package com.karbonpowered.api

interface MinecraftVersion : Comparable<MinecraftVersion> {
    val name: String
    val protocol: Int

    override fun compareTo(other: MinecraftVersion): Int = protocol.compareTo(other.protocol)
}

