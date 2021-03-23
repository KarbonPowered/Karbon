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
    override suspend fun decode(input: Input): T

    /**
     * Encodes a [Message] into a [Output].
     *
     * @param byteWriteChannel the output to encode into.
     * @param data The message to encode
     */
    override suspend fun encode(output: Output, data: T)

    data class CodecRegistration<M : Message>(
        val opcode: Int,
        val codec: MessageCodec<in M>
    )
}

data class CodecBuilder<T : Message>(val messageType: KClass<T>) {
    var decode: Input.() -> T? = { null }
    var encode: Output.(T) -> Unit = {}

    fun decode(decode: Input.() -> T?) {
        this.decode = decode
    }

    fun encode(encode: Output.(T) -> Unit) {
        this.encode = encode
    }

    fun build(): MessageCodec<T> = object : MessageCodec<T> {
        val decoder: Input.() -> T? = decode
        val encoder: Output.(T) -> Unit = encode
        override val messageType: KClass<T> = this@CodecBuilder.messageType

        override suspend fun decode(input: Input): T? = decoder(input)

        override suspend fun encode(output: Output, message: T) = encoder(output, message)
    }
}

inline fun <reified T : Message> Codec(builder: CodecBuilder<T>.()->Unit) = CodecBuilder(T::class).apply(builder).build()