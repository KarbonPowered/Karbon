package com.karbonpowered.network.protocol

import com.karbonpowered.common.Named
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.network.Message
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

expect interface Protocol : Named {
    /**
     * The name of this Protocol
     */
    override val name: String

    suspend fun readHeader(input: ByteReadChannel): Pair<Int, MessageCodec<*>>

    suspend fun writeHeader(output: ByteWriteChannel, codec: MessageCodec.CodecRegistration<*>, data: ByteReadPacket)

    fun <M : Message> getCodecRegistration(message: KClass<M>): MessageCodec.CodecRegistration<M>?
}

inline fun <reified M : Message> Protocol.getCodecRegistration(): MessageCodec.CodecRegistration<M>? =
    getCodecRegistration(M::class)

operator fun <M : Message> Protocol.get(message: KClass<M>): MessageCodec.CodecRegistration<M>? =
    getCodecRegistration(message)
