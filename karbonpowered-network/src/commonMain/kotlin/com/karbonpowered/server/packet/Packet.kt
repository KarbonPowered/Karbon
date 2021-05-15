package com.karbonpowered.server.packet

import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

interface Packet {
    val isPriority: Boolean get() = false
}

interface PacketCodec<T : Packet> {
    val packetType: KClass<T>

    fun encode(output: Output, packet: T)
    fun decode(input: Input): T
}

fun interface PacketHandler {
    fun handle(packet: Packet)
}

