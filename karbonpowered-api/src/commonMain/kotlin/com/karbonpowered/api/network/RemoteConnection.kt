package com.karbonpowered.api.network

import io.ktor.util.network.*

interface RemoteConnection {
    val address: NetworkAddress

    val virtualHost: NetworkAddress

    fun close()
}