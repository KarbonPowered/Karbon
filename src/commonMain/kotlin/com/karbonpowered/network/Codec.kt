package com.karbonpowered.network

import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

interface PacketEncoder<T : Packet> {
    fun encode(output: Output, packet: T)
}

interface PacketDecoder<T : Packet> {
    fun decode(input: Input): T
}

interface PacketCodec<T : Packet> : PacketEncoder<T>, PacketDecoder<T> {
    val packetType: KClass<T>
}