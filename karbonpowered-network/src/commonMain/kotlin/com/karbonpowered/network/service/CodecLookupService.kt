package com.karbonpowered.network.service

import com.karbonpowered.network.Codec
import com.karbonpowered.network.Message
import kotlinx.atomicfu.atomic
import kotlin.reflect.KClass

class CodecLookupService(
    size: Int
) {
    private val messages = mutableMapOf<KClass<out Message>, Codec.CodecRegistration<*>>()
    private val opcodes =
        if (size > 0) null else mutableMapOf<Int, Codec<*>>()
    private val opcodeTable = if (size > 0) arrayOfNulls<Codec<*>>(size) else null
    private var nextInt by atomic(0)

    @Suppress("UNCHECKED_CAST")
    fun <M : Message> bind(
        messageKClass: KClass<M>,
        codec: Codec<in M>,
        opcode: Int = nextInt++
    ): Codec.CodecRegistration<M> {
        val registration = messages[messageKClass] as? Codec.CodecRegistration<M>
        if (registration != null) {
            return registration
        }
        require(opcode >= 0) { "Opcode must either greater than or equal to 0" }
        return Codec.CodecRegistration(opcode, codec).also {
            messages[messageKClass] = it
        }
    }

    private fun put(opcode: Int, codec: Codec<*>) {
        if (opcodeTable != null && opcodes == null) {
            opcodeTable[opcode] = codec
        } else if (opcodes != null && opcodeTable == null) {
            opcodes[opcode] = codec
        }
    }

    operator fun get(opcode: Int): Codec<*>? {
        return if (opcodeTable != null && opcodes == null) {
            opcodeTable[opcode]
        } else if (opcodes != null && opcodeTable == null) {
            opcodes[opcode]
        } else null
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <M : Message> get(message: KClass<M>): Codec.CodecRegistration<M>? = messages[message] as? Codec.CodecRegistration<M>
}