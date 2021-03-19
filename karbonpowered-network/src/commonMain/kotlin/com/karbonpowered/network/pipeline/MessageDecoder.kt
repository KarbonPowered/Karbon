package com.karbonpowered.network.pipeline

import com.karbonpowered.network.Message
import com.karbonpowered.network.exception.UnknownPacketException
import io.ktor.utils.io.*

class MessageDecoder(
    val connectionHandler: ConnectionHandler
) {
    suspend fun decode(input: ByteReadChannel): Message? {
        val protocol = connectionHandler.session.protocol
        val codec = try {
            protocol.readHeader(input)
        } catch (e: UnknownPacketException) {
            val length = e.length
            if (length > 0) {
                input.readRemaining(length.toLong())
            }
            throw e
        }
        return codec.decode(input.readPacket(input.availableForRead))
    }
}