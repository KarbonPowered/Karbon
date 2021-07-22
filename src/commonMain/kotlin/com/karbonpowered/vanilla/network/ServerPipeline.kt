package com.karbonpowered.vanilla.network

import com.karbonpowered.network.PacketCodec
import com.karbonpowered.network.readVarInt
import com.karbonpowered.network.writeVarInt
import com.karbonpowered.vanilla.VanillaSession
import com.karbonpowered.vanilla.network.protocol.VanillaPacket
import io.ktor.util.pipeline.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val receivePipelineCoroutine = CoroutineName("receive-pipeline")
val sendPipelineCoroutine = CoroutineName("send-pipeline")

fun CoroutineScope.startReceivePipeline(
    session: VanillaSession,
    pipeline: Pipeline<Any, VanillaSession>
) = launch(receivePipelineCoroutine) {
    session.protocol.state = VanillaProtocol.ProtocolState.HANDSHAKE
    while (true) {
        val input = session.connection.input
        val packetSize = input.readVarInt()
        val rawData = input.readPacket(packetSize)
        val packetId = rawData.readVarInt()
        val codec = session.protocol.incomingCodec(packetId)
        val packet = codec.decode(rawData)
        pipeline.execute(session, packet)
    }
}

fun CoroutineScope.startSendPipeline(
    session: VanillaSession,
    pipeline: Pipeline<Any, VanillaSession>
) = launch(sendPipelineCoroutine) {
    while (true) {
        val packet = session.sendChannel.receive()
        val result = pipeline.execute(session, packet)
        if (result is VanillaPacket) {
            val codec = session.protocol.outgoingCodec(result::class) as PacketCodec<VanillaPacket>
            val packetId = session.protocol.outgoingId(codec)
            session.connection.output.writePacket {
                val encodedPacket = buildPacket {
                    writeVarInt(packetId)
                    codec.encode(this, result)
                }
                writeVarInt(encodedPacket.remaining.toInt())
                writePacket(encodedPacket)
            }
            session.connection.output.flush()
        }
    }
}
