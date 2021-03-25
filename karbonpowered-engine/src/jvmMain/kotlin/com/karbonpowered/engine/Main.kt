package com.karbonpowered.engine

import com.karbonpowered.engine.network.KarbonServer
import io.ktor.util.network.*
import kotlinx.coroutines.runBlocking


fun main() {
    runBlocking {
        Engine.server = KarbonServer()
        Engine.server.bind(NetworkAddress("localhost", 25565)).join()
    }
}