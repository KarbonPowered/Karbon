package com.karbonpowered.network.netty

import com.karbonpowered.server.Session
import com.karbonpowered.server.util.VarInitByteDecoder
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageCodec
import io.netty.handler.codec.DecoderException

class NettyTcpPacketSizer(
    val session: Session
) : ByteToMessageCodec<ByteBuf>() {
    val reader = VarInitByteDecoder()

    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        val length = msg.readableBytes()
        out.ensureWritable(session.packetProtocol.packetHeader.lengthSize(length) + length)
        out.writeVarInt(length)
        out.writeBytes(msg)
    }

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (!ctx.channel().isActive) {
            buf.clear()
            return
        }

        reader.reset()
        val varIntEnd = buf.forEachByte { reader.process(it.toInt()) }
        if (varIntEnd == -1) {
            // We tried to go beyond the end of the buffer. This is probably a good sign that the
            // buffer was too short to hold a proper varint.
            return
        }

        if (reader.result == VarInitByteDecoder.DecodeResult.SUCCESS) {
            val readLen = reader.readVarInt
            if (readLen < 0) {
                throw DecoderException("Bad packet length")
            } else if (readLen == 0) {
                // skip over the empty packet and ignore it
                buf.readerIndex(varIntEnd + 1)
            } else {
                val minimumRead: Int = reader.bytesRead + readLen
                if (buf.isReadable(minimumRead)) {
                    val slicedBuf = buf.retainedSlice(varIntEnd + 1, readLen)
                    out.add(slicedBuf)
                    buf.skipBytes(minimumRead)
                }
            }
        } else if (reader.result == VarInitByteDecoder.DecodeResult.TOO_BIG) {
            throw DecoderException("Varint too big")
        }
    }
}
