package com.karbonpowered.protocol

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.network.Message
import com.karbonpowered.network.MessageHandler
import com.karbonpowered.network.protocol.AbstractProtocol
import com.karbonpowered.network.service.CodecLookupService
import com.karbonpowered.network.service.HandlerLookupService
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

abstract class MinecraftProtocol(name: String, val isServer: Boolean) : AbstractProtocol(name) {
    val clientboundCodecLookupService = CodecLookupService()
    val serverboundCodecLookupService = CodecLookupService()
    val handlerLookupService = HandlerLookupService()

    override suspend fun readHeader(input: ByteReadChannel): Pair<Int, MessageCodec<*>> {
        TODO()
    }

    override suspend fun writeHeader(
        output: ByteWriteChannel,
        codec: MessageCodec.CodecRegistration<*>,
        data: ByteReadPacket
    ) {
        output.writePacket {
            writeVarInt((buildPacket {
                writeVarInt(codec.opcode)
            }.remaining + data.remaining).toInt())
            writeVarInt(codec.opcode)
        }
    }

    override fun <M : Message> getCodecRegistration(message: KClass<M>): MessageCodec.CodecRegistration<M>? =
        clientboundCodecLookupService[message] ?: serverboundCodecLookupService[message]

    inline fun <reified M : MinecraftPacket> serverbound(
        opcode: Int,
        codec: MessageCodec<M>,
        handler: MessageHandler<*, M>? = null
    ) = serverbound(opcode, M::class, codec, handler)

    fun <M : MinecraftPacket> serverbound(
        opcode: Int,
        packet: KClass<M>,
        codec: MessageCodec<M>,
        handler: MessageHandler<*, M>? = null
    ) {
        serverboundCodecLookupService.bind(packet, codec, opcode)
        if (handler != null) {
            handlerLookupService.bind(codec.messageType, handler)
        }
    }

    inline fun <reified M : MinecraftPacket> clientbound(
        opcode: Int,
        codec: MessageCodec<M>,
        handler: MessageHandler<*, M>? = null
    ) = clientbound(opcode, M::class, codec, handler)

    fun <M : MinecraftPacket> clientbound(
        opcode: Int,
        packet: KClass<M>,
        codec: MessageCodec<M>,
        handler: MessageHandler<*, M>? = null
    ) {
        clientboundCodecLookupService.bind(packet, codec, opcode)
        if (handler != null) {
            handlerLookupService.bind(codec.messageType, handler)
        }
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

    companion object
}