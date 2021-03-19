package com.karbonpowered.engine

import com.karbonpowered.engine.network.KarbonServer
import io.ktor.util.network.*
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        KarbonServer().bind(NetworkAddress("localhost", 25565)).join()
    }
}