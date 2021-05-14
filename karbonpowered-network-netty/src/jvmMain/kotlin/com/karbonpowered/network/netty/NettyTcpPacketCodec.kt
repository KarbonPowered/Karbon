package com.karbonpowered.network.netty

import com.karbonpowered.server.Session
import com.karbonpowered.server.event.PacketErrorEventImpl
import com.karbonpowered.server.packet.Packet
import com.karbonpowered.server.packet.PacketCodec
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageCodec

class NettyTcpPacketCodec(
    val session: Session
) : ByteToMessageCodec<Packet>() {
    override fun encode(ctx: ChannelHandlerContext, packet: Packet, out: ByteBuf) {
        val initial = out.writerIndex()

        try {
            val codec = session.packetProtocol.outgoingCodec(packet::class) as PacketCodec<Packet>
            val id = session.packetProtocol.outgoingId(codec)
            out.writeVarInt(id)
            codec.encode(out.asOutput(), packet)
        } catch (t: Throwable) {
            out.writerIndex(initial)

            val event = PacketErrorEventImpl(session, t)
            session.callEvent(event)
            if (!event.shouldSuppress) {
                throw t
            }
        }
    }

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        val initial = buf.readerIndex()

        try {
            val id = buf.readVarInt()

            if (id == -1) {
                buf.readerIndex(initial)
                return
            }

            val codec = session.packetProtocol.incomingCodec(id)
            val packet = codec.decode(buf.asInput())

            if (buf.readableBytes() > 0) {
                throw IllegalStateException("Packet '${packet::class.simpleName}' not fully read. Extra bytes: ${buf.readableBytes()}")
            }

            out.add(packet)
        } catch (t: Throwable) {
            // Advance buffer to end to make sure remaining data in this packet is skipped.
            buf.readerIndex(buf.readerIndex() + buf.readableBytes())

            val event = PacketErrorEventImpl(session, t)
            session.callEvent(event)
            if (!event.shouldSuppress) {
                throw t
            }
        }
    }
}