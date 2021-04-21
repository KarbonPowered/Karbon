package com.karbonpowered.network.protocol

import com.karbonpowered.common.Named
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.network.Message
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

actual interface Protocol : Named {
    /**
     * The name of this Protocol
     */
    actual override val name: String

    actual suspend fun readHeader(input: ByteReadChannel): Pair<Int, MessageCodec<*>>

    actual suspend fun writeHeader(
        output: ByteWriteChannel,
        codec: MessageCodec.CodecRegistration<*>,
        data: ByteReadPacket
    )

    actual fun <M : Message> getCodecRegistration(message: KClass<M>): MessageCodec.CodecRegistration<M>?

    fun <M : Message> getCodecRegistration(message: Class<M>): MessageCodec.CodecRegistration<M>? =
        getCodecRegistration(message.kotlin)
}

operator fun <M : Message> Protocol.get(message: Class<M>): MessageCodec.CodecRegistration<M>? =
    getCodecRegistration(message)