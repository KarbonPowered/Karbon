package com.karbonpowered.network

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

/**
 * [Codec]s are used to encode/decode a [Input]/[Message] into [Message]/[Output].
 */
interface Codec<T : Message> {
    /**
     * Decodes a [Input] into a [Message].
     *
     * @param byteReadChannel the input read from
     * @return the message fully encoded.
     */
    suspend fun decode(input: Input): T?

    /**
     * Encodes a [Message] into a [Output].
     *
     * @param byteWriteChannel the output to encode into.
     * @param message The message to encode
     */
    suspend fun encode(output: Output, message: T)

    data class CodecRegistration<M : Message>(
        val opcode: Int,
        val codec: Codec<in M>
    )
}

