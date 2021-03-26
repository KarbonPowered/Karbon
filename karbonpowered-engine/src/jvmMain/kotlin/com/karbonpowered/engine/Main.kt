package com.karbonpowered.engine

import com.karbonpowered.engine.network.KarbonServer
import com.karbonpowered.nbt.NBT
import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking


fun main() = runBlocking {
    Engine.server = KarbonServer().apply {
        val codec = createDimensionCodec()

        val packet = buildPacket {
            runBlocking { NBT.encode(this@buildPacket,codec) }
        }

        val decoded = runBlocking { NBT.decode(packet) }

        println(decoded)

    }


    val packet = createTestNbt()

    val decode = NBT.decode(packet)
    println(decode)


//        Engine.server.bind(NetworkAddress("localhost", 25565)).join()
}

fun createTestNbt() = runBlocking {
    buildPacket {
        NBT.encode(this, NBT(
            "test" to NBT(
                "nested" to 91,
                "kek" to "lol"
            )
        ))
    }
}
