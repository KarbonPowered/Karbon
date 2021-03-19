package com.karbonpowered.protocol

import com.karbonpowered.network.Codec
import com.karbonpowered.network.Message
import com.karbonpowered.network.MessageHandler
import com.karbonpowered.network.protocol.AbstractProtocol
import com.karbonpowered.network.service.CodecLookupService
import com.karbonpowered.network.service.HandlerLookupService
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

abstract class MinecraftProtocol(name: String, highestOpcode: Int = -1) : AbstractProtocol(name) {
    val codecLookupService = CodecLookupService(highestOpcode + 1)
    val handlerLookupService = HandlerLookupService()

    override suspend fun readHeader(input: ByteReadChannel): Codec<*> {
        val varIntByteDecoder = VarIntByteDecoder()
        input.readVarInt(varIntByteDecoder) ?: throw RuntimeException("Invalid length!")
        varIntByteDecoder.reset()
        val opcode = input.readVarInt(varIntByteDecoder) ?: throw RuntimeException("Invalid opcode!")
        return codecLookupService[opcode]!!
    }

    override suspend fun writeHeader(
        output: ByteWriteChannel,
        codec: Codec.CodecRegistration<*>,
        data: ByteReadPacket
    ) {
        output.writePacket {
            writeVarInt((buildPacket {
                writeVarInt(codec.opcode)
            }.remaining + data.remaining).toInt())
            writeVarInt(codec.opcode)
        }
    }

    override fun <M : Message> getCodecRegistration(message: KClass<M>): Codec.CodecRegistration<M>? =
        codecLookupService[message]

    fun <M : MinecraftPacket> bind(
        opcode: Int,
        packet: KClass<M>,
        codec: Codec<M>,
        handler: MessageHandler<*, M>? = null
    ) {
        codecLookupService.bind(packet, codec, opcode)
        if (handler != null) {
            registerHandler(codec, handler)
        }
    }

    fun <M : MinecraftPacket> registerHandler(codec: Codec<M>, handler: MessageHandler<*, M>) {
        handlerLookupService.bind(codec.messageType, handler)
    }

    private suspend fun ByteReadChannel.readVarInt(decoder: VarIntByteDecoder): Int? {
        while (true) {
            if (!decoder.process(readByte().toInt())) {
                break
            }
        }
        return if (decoder.result != VarIntByteDecoder.Result.SUCCESS) {
            null
        } else {
            decoder.readVarInt
        }
    }
}

inline fun <reified M : MinecraftPacket> MinecraftProtocol.bind(opcode: Int, codec: Codec<M>) =
    bind(opcode, M::class, codec)