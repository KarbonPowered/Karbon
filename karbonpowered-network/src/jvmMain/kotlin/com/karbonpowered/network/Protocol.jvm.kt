package com.karbonpowered.network

import com.karbonpowered.common.Named
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

actual interface Protocol : Named {
    /**
     * The name of this Protocol
     */
    actual override val name: String

    actual fun readHeader(input: Input): Codec<*>

    actual fun writeHeader(output: BytePacketBuilder, codec: Codec.CodecRegistration<*>, data: BytePacketBuilder)

    actual fun <M : Message> getCodecRegistration(message: KClass<M>): Codec.CodecRegistration<M>?

    fun <M : Message> getCodecRegistration(message: Class<M>): Codec.CodecRegistration<M>? =
        getCodecRegistration(message.kotlin)
}

operator fun <M : Message> Protocol.get(message: Class<M>): Codec.CodecRegistration<M>? = getCodecRegistration(message)