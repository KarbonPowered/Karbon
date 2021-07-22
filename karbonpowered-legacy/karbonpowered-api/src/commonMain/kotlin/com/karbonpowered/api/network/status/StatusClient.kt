package com.karbonpowered.api.network.status

import com.karbonpowered.api.MinecraftVersion
import io.ktor.util.network.*

interface StatusClient {
    val address: NetworkAddress
    val version: MinecraftVersion
    val virtualHost: NetworkAddress
}