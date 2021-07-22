package com.karbonpowered.server.cio
//
//import Session
//import Packet
//import PacketCodec
//import readVarInt
//import writeVarInt
//import io.ktor.utils.io.*
//import io.ktor.utils.io.core.*
//import io.ktor.utils.io.core.internal.*
//
//suspend fun CioTcpSession.writePacket(packet: Packet, flush: Boolean = true) {
//    if (connection.output.isClosedForWrite) {
//        return
//    }
//    val codec = packetProtocol.outgoingCodec(packet::class) as PacketCodec<Packet>
//    val packetId = packetProtocol.outgoingId(codec)
//    val packetData = buildPacket {
//        packetProtocol.packetHeader.writePacketId(this, packetId)
//        codec.encode(this, packet)
//    }
//    connection.output.writeVarInt(packetData.remaining.toInt())
//    connection.output.writePacket(packetData)
//    if (flush) {
//        connection.output.flush()
//    }
//}
//
//@OptIn(DangerousInternalIoApi::class, ExperimentalIoApi::class)
//suspend fun CioCioTcpServerSession.parsePacket(): Packet? {
//    val length = connection.input.readVarInt()
//    val lengthSize = packetProtocol.packetHeader.lengthSize(length)
//    val packetId = connection.input.readVarInt()
//    val packetData = connection.input.readPacket(length-lengthSize)
//    val codec = try {
//        packetProtocol.incomingCodec(packetId)
//    } catch (e: IllegalArgumentException) {
//        return null
//    }
//    val packet = try {
//        codec.decode(packetData)
//    } catch (t: Throwable) {
//        t.printStackTrace()
//        return null
//    }
//    if (packetData.remaining > 0) {
//        println("Some ${packetData.remaining} extra bytes after packet (length=$length,packetId=$packetId): $packet")
//    }
//    return packet
//}
//
//private suspend fun parseLength(session: Session, input: ByteReadChannel): Int {
//    val size = session.packetProtocol.packetHeader.lengthSize
//    if (size > 0) {
//        val lengthBytes = ByteArray(size)
//        repeat(size) { index ->
//            if (!input.isClosedForRead) {
//                lengthBytes[index] = input.readByte()
//                if ((session.packetProtocol.packetHeader.isLengthVariable && lengthBytes[index] >= 0) || index == size - 1) {
//                    val length = session.packetProtocol.packetHeader.readLength(
//                        ByteReadPacket(lengthBytes),
//                        input.availableForRead
//                    )
//                    if (input.availableForRead < length) {
//                        throw IllegalStateException("Invalid packet length")
//                    }
//                    return length
//                }
//            }
//        }
//        throw IllegalStateException("Invalid packet length")
//    } else return input.availableForRead
//}