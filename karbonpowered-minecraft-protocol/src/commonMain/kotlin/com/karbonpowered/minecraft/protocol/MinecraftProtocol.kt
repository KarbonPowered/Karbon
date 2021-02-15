package com.karbonpowered.minecraft.protocol

import com.karbonpowered.network.Codec
import com.karbonpowered.network.Message
import com.karbonpowered.network.protocol.AbstractProtocol
import com.karbonpowered.network.service.CodecLookupService
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

abstract class MinecraftProtocol(name: String, highestOpcode: Int) : AbstractProtocol(name) {
    private val codecLookupService = CodecLookupService(highestOpcode + 1)

    fun <M : MinecraftPacket> bind(opcode: Int, packet: KClass<M>, codec: Codec<M>) {
        codecLookupService.bind(packet, codec, opcode)
    }

    override suspend fun readHeader(input: ByteReadChannel): Codec<*> {
        val length = input.readVarInt()
        val opcode = input.readVarInt()
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
}

inline fun <reified M : MinecraftPacket> MinecraftProtocol.bind(opcode: Int, codec: Codec<M>) =
    bind(opcode, M::class, codec)