package com.karbonpowered.network

import com.karbonpowered.io.Codec
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

/**
 * [MessageCodec]s are used to encode/decode a [Input]/[Message] into [Message]/[Output].
 */
interface MessageCodec<T : Message> : Codec<T> {
    val messageType: KClass<T>

    /**
     * Decodes a [Input] into a [Message].
     *
     * @param byteReadChannel the input read from
     * @return the message fully encoded.
     */
    override fun decode(input: Input): T

    /**
     * Encodes a [Message] into a [Output].
     *
     * @param byteWriteChannel the output to encode into.
     * @param data The message to encode
     */
    override fun encode(output: Output, data: T)

    data class CodecRegistration<M : Message>(
        val opcode: Int,
        val codec: MessageCodec<in M>
    )
}