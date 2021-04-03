package com.karbonpowered.engine

import com.karbonpowered.engine.network.KarbonServer
import io.ktor.util.network.*
import kotlinx.coroutines.runBlocking

fun main() {
    Engine.server = KarbonServer()
    runBlocking {
        Engine.server.bind(NetworkAddress("0.0.0.0", 25565)).join()
    }
}