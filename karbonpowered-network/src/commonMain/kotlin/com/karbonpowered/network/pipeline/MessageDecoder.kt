package com.karbonpowered.network.pipeline

import com.karbonpowered.network.Message
import com.karbonpowered.network.exception.UnknownPacketException
import io.ktor.utils.io.*

class MessageDecoder(
    val connectionHandler: ConnectionHandler
) {
    suspend fun decode(input: ByteReadChannel): Message? {
        val protocol = connectionHandler.session.protocol
        val (length, codec) = try {
            protocol.readHeader(input)
        } catch (e: UnknownPacketException) {
            val length = e.length
            if (length > 0) {
                input.readRemaining(length.toLong())
            }
            throw e
        }
        println("codec: $codec, length: $length available:${input.availableForRead}")
        val packet = input.readPacket(length)
        val decode = codec.decode(packet)
        return decode
    }
}