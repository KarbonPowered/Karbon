package com.karbonpowered.engine

import com.karbonpowered.engine.network.KarbonServer
import io.ktor.util.network.*
import kotlinx.coroutines.runBlocking

fun main() {
    println("Starting KarbonPowered server...")
    Engine.server = KarbonServer()
    runBlocking {
        Engine.server.bind(NetworkAddress("0.0.0.0", 25566)).join()
    }
}