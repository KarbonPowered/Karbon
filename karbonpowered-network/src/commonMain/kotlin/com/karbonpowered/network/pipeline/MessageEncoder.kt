package com.karbonpowered.network.pipeline

import com.karbonpowered.network.Codec
import com.karbonpowered.network.Message
import com.karbonpowered.network.protocol.get
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

class MessageEncoder(
    val connectionHandler: ConnectionHandler
) {
    @Suppress("UNCHECKED_CAST")
    suspend fun encode(output: ByteWriteChannel, message: Message) {
        val protocol = connectionHandler.session.protocol
        val codecRegistration = protocol[message::class] as? Codec.CodecRegistration<Message>
            ?: throw Exception("Unknown message type: ${message::class}")
        val data = buildPacket {
            codecRegistration.codec.encode(this, message)
        }
        protocol.writeHeader(output, codecRegistration, data)
        output.writePacket(data)
    }
}