package com.karbonpowered.api

interface MinecraftVersion : Comparable<MinecraftVersion> {
    val name: String
    val isLegacy: Boolean
    val protocolVersion: Int
}