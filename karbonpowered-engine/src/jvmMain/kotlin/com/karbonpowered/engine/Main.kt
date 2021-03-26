package com.karbonpowered.engine

import com.karbonpowered.engine.network.KarbonServer
import com.karbonpowered.nbt.NBT
import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking


fun main() {
    Engine.server = KarbonServer()
    runBlocking {
        Engine.server.bind(NetworkAddress("localhost",25565)).join()
    }
}