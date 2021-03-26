package com.karbonpowered.engine.network

import com.karbonpowered.network.Message
import com.karbonpowered.network.MessageCodec
import com.karbonpowered.network.Session
import com.karbonpowered.network.protocol.Protocol
import com.karbonpowered.protocol.MinecraftProtocol
import com.karbonpowered.protocol.readVarInt
import com.karbonpowered.protocol.writeVarInt
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.experimental.and

class KarbonSession(
    override val connection: Connection,
    override var protocol: Protocol
) : Session {
    val context = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val channel = Channel<Array<out Message>>(Channel.UNLIMITED)

    init {
        channel.receiveAsFlow().onEach { messages ->
            messages.forEach { message ->
                val codecRegistration =
                    (protocol as MinecraftProtocol).clientboundCodecLookupService[message::class] as? MessageCodec.CodecRegistration<Message>
                        ?: return@forEach
                connection.output.writePacket {
                    val data = buildPacket {
                        writeVarInt(codecRegistration.opcode)
                        codecRegistration.codec.encode(this, message)
                    }
                    println("OUT: opcode=${codecRegistration.opcode} length=${data.remaining} $message")
                    writeVarInt(data.remaining.toInt())
                    writePacket(data)
                }
                connection.output.flush()
            }
        }.launchIn(context)

        context.launch {
            try {
                while (true) {
                    val length = connection.input.readVarInt()
                    val data = connection.input.readPacket(length)
                    val opcode = data.readVarInt()
                    val codec = (protocol as MinecraftProtocol).serverboundCodecLookupService[opcode] ?: continue
                    val message = codec.decode(data) ?: continue
                    try {
                        messageReceived(message)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: ClosedReceiveChannelException) {
            }
        }
    }

    override suspend fun <T : Message> messageReceived(message: T) {
        println("IN : $message")
        (protocol as MinecraftProtocol).handlerLookupService[message::class]?.handle(this, message)
    }

    override suspend fun send(vararg messages: Message) {
        channel.send(messages)
    }

    override fun disconnect() {
    }

    override fun onDisconnect() {
    }

    override fun onReady() {
    }

    override fun onInboundThrowable(throwable: Throwable) {
    }

    private suspend fun ByteReadChannel.readVarInt(): Int {
        var numRead = 0
        var result = 0
        var read: Byte
        do {
            read = readByte()
            val value = (read and 127).toInt()
            result = result or (value shl 7 * numRead)
            numRead++
            if (numRead > 5) {
                throw RuntimeException("VarInt is too big")
            }
        } while (read and 128.toByte() != 0.toByte())
        return result
    }
}