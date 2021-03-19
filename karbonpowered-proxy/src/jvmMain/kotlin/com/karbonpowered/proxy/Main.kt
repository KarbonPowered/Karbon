package com.karbonpowered.proxy

import io.ktor.util.network.*
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        KarbonProxy().bind(NetworkAddress("localhost", 25565)).join()
    }
}