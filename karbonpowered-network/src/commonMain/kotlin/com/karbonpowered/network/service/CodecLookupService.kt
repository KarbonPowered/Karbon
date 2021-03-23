package com.karbonpowered.network.service

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.network.Message
import kotlinx.atomicfu.atomic
import kotlin.reflect.KClass

class CodecLookupService(
    size: Int = 0
) {
    private val messages = mutableMapOf<KClass<out Message>, MessageCodec.CodecRegistration<*>>()
    private val opcodes =
        if (size > 0) null else mutableMapOf<Int, MessageCodec<*>>()
    private val opcodeTable = if (size > 0) arrayOfNulls<MessageCodec<*>>(size) else null
    private var nextInt by atomic(0)

    @Suppress("UNCHECKED_CAST")
    fun <M : Message> bind(
        messageKClass: KClass<M>,
        codec: MessageCodec<in M>,
        opcode: Int = nextInt++
    ): MessageCodec.CodecRegistration<M> {
        val registration = messages[messageKClass] as? MessageCodec.CodecRegistration<M>
        if (registration != null) {
            return registration
        }
        require(opcode >= 0) { "Opcode must either greater than or equal to 0" }
        return MessageCodec.CodecRegistration(opcode, codec).also {
            messages[messageKClass] = it
            put(opcode, codec)
        }
    }

    private fun put(opcode: Int, codec: MessageCodec<*>) {
        if (opcodeTable != null && opcodes == null) {
            opcodeTable[opcode] = codec
        } else if (opcodes != null && opcodeTable == null) {
            opcodes[opcode] = codec
        }
    }

    operator fun get(opcode: Int): MessageCodec<*>? {
        return if (opcodeTable != null && opcodes == null) {
            opcodeTable[opcode]
        } else if (opcodes != null && opcodeTable == null) {
            opcodes[opcode]
        } else null
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <M : Message> get(message: KClass<M>): MessageCodec.CodecRegistration<M>? =
        messages[message] as? MessageCodec.CodecRegistration<M>

    override fun toString(): String = "CodecLookupService(messages=$messages)"
}