package com.karbonpowered.minecraft.protocol

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

suspend fun ByteReadChannel.readVarInt(): Int {
    TODO()
}

suspend fun BytePacketBuilder.writeVarInt(int: Int) {
    TODO()
}