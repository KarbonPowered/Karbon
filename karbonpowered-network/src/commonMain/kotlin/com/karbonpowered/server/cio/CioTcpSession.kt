package com.karbonpowered.server.cio
//
//import com.karbonpowered.server.Session
//import com.karbonpowered.server.event.*
//import com.karbonpowered.server.packet.Packet
//import com.karbonpowered.server.packet.PacketProtocol
//import io.ktor.network.sockets.*
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.channels.ClosedReceiveChannelException
//import kotlinx.coroutines.launch
//import kotlin.coroutines.CoroutineContext
//
//abstract class CioTcpSession(
//    override val coroutineContext: CoroutineContext
//) : Session {
//    protected var disconnected = false
//    private val _listeners = ArrayList<SessionListener>()
//    override val listeners: Collection<SessionListener>
//        get() = _listeners
//
//    override fun addListener(listener: SessionListener) {
//        _listeners.add(listener)
//    }
//
//    override fun removeListener(listener: SessionListener) {
//        _listeners.remove(listener)
//    }
//
//    override fun send(packet: Packet) {
///*        if (connection.output.isClosedForWrite) {
//            return
//        }*/
//        val sendingEvent = PacketSendingEventImpl(this, packet)
//        callEvent(sendingEvent)
//
//        if (!sendingEvent.isCancelled) {
//            val toSend = sendingEvent.packet
//            launch {
//                writePacket(toSend)
//                callEvent(PacketSentEventImpl(this@CioTcpSession, toSend))
//            }
//        }
//    }
//
//    override fun disconnect(reason: String?, cause: Throwable?) {
//        if (disconnected) {
//            return
//        }
//
//        disconnected = true
//
//        if (isConnected) {
//            callEvent(DisconnectingEventImpl(this, reason ?: "Connection closed", cause))
////            connection.socket.close()
////            launch {
////                connection.socket.awaitClosed()
////                callEvent(DisconnectedEventImpl(this@CioTcpSession, reason ?: "Connection closed", cause))
////            }
//        } else {
//            callEvent(DisconnectedEventImpl(this, reason ?: "Connection closed", cause))
//        }
//    }
//
//    override fun callEvent(event: SessionEvent) {
//        try {
//            _listeners.forEach {
//                event.call(it)
//            }
//        } catch (t: Throwable) {
//            exceptionCaught(t)
//        }
//    }
//
//    override fun exceptionCaught(cause: Throwable) {
//        cause.printStackTrace()
//        val message = cause.message.toString()
//        disconnect(message, cause)
//    }
//}
//
////class CioCioTcpServerSession(
////    connection: Connection,
////    override var packetProtocol: PacketProtocol,
////    coroutineContext: CoroutineContext
////) : CioTcpSession(connection, coroutineContext) {
////    suspend fun startChannelRead() = launch {
////        try {
////            callEvent(ConnectedEventImpl(this@CioCioTcpServerSession))
////            while (true) {
////                val packet = parsePacket()
////                if (packet != null) {
////                    callEvent(PacketReceivedEventImpl(this@CioCioTcpServerSession, packet))
////                }
////            }
////        } catch (e: ClosedReceiveChannelException) {
////            coroutineContext.cancel()
////        } catch (cause: Throwable) {
////            exceptionCaught(cause)
////        }
////    }
////}