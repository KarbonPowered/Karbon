package com.karbonpowered.engine.network

import com.karbonpowered.engine.Engine
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
import io.ktor.utils.io.core.internal.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import kotlin.experimental.and

class KarbonSession(
    override val connection: Connection,
    override var protocol: Protocol
) : Session {
    val context = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override suspend fun <T : Message> messageReceived(message: T) {
//        println("IN : $message")
        (protocol as MinecraftProtocol).handlerLookupService[message::class]?.handle(this, message)
    }

    @OptIn(DangerousInternalIoApi::class)
    override suspend fun send(vararg messages: Message) {
        messages.forEach { message ->
            val codecRegistration =
                (protocol as MinecraftProtocol).clientboundCodecLookupService[message::class] as? MessageCodec.CodecRegistration<Message>
                    ?: return@forEach
            val packet = buildPacket {
                writeVarInt(codecRegistration.opcode)
                codecRegistration.codec.encode(this, message)
            }
            connection.output.writeVarInt(packet.remaining.toInt())
            connection.output.writePacket(packet)
        }
        connection.output.flush()
    }

    override fun disconnect() {
    }

    override fun onDisconnect() {
    }

    override fun onReady() {
        context.launch {
            try {
                while (!connection.socket.isClosed) {
                    val length = connection.input.readVarInt()
                    val data = connection.input.readPacket(length)
                    val opcode = data.readVarInt()
                    val codec = (protocol as MinecraftProtocol).serverboundCodecLookupService[opcode] ?: continue
                    val message = codec.decode(data)
                    try {
                        messageReceived(message)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: ClosedReceiveChannelException) {
            }
            Engine.server.connectionHandler.connectionInactive(connection)
        }
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