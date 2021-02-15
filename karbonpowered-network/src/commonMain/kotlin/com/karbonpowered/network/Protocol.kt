package com.karbonpowered.network

import com.karbonpowered.common.Named
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

expect interface Protocol : Named {
    /**
     * The name of this Protocol
     */
    override val name: String

    suspend fun readHeader(input: ByteReadChannel): Codec<*>

    suspend fun writeHeader(output: ByteWriteChannel, codec: Codec.CodecRegistration<*>, data: BytePacketBuilder)

    fun <M : Message> getCodecRegistration(message: KClass<M>): Codec.CodecRegistration<M>?
}

inline fun <reified M : Message> Protocol.getCodecRegistration(): Codec.CodecRegistration<M>? =
    getCodecRegistration(M::class)

operator fun <M : Message> Protocol.get(message: KClass<M>): Codec.CodecRegistration<M>? = getCodecRegistration(message)
