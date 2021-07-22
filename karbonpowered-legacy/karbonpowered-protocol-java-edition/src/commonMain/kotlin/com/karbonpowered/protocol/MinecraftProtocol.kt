package com.karbonpowered.protocol

import com.karbonpowered.api.MinecraftVersion
import com.karbonpowered.server.Session
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.packet.PacketProtocol

abstract class MinecraftProtocol(val version: MinecraftVersion, val isServer: Boolean) : PacketProtocol() {
    var useDefaultListener: Boolean = true
    private lateinit var handshakeBuilder: (ProtocolBuilder) -> Unit
    private lateinit var statusBuilder: (ProtocolBuilder) -> Unit
    private lateinit var loginBuilder: (ProtocolBuilder) -> Unit
    private lateinit var gameBuilder: (ProtocolBuilder) -> Unit
    private var newServerSessionBlock: (Session) -> Unit = {}

    var subProtocol: SubProtocol = SubProtocol.HANDSHAKE
        set(value) {
            field = value
            clearPackets()
            when (value) {
                SubProtocol.HANDSHAKE -> handshakeBuilder(ProtocolBuilder())
                SubProtocol.STATUS -> statusBuilder(ProtocolBuilder())
                SubProtocol.LOGIN -> loginBuilder(ProtocolBuilder())
                SubProtocol.GAME -> gameBuilder(ProtocolBuilder())
            }
        }

    fun onNewServerSession(block: (Session) -> Unit) {
        newServerSessionBlock = block
    }

    override fun newServerSession(session: Session) {
        subProtocol = SubProtocol.HANDSHAKE
        if (useDefaultListener) {
            session.addListener(ServerListener())
        }
        newServerSessionBlock(session)
    }

    enum class SubProtocol {
        HANDSHAKE,
        STATUS,
        LOGIN,
        GAME;
    }

    fun handshake(block: ProtocolBuilder.() -> Unit) {
        handshakeBuilder = block
    }

    fun status(block: ProtocolBuilder.() -> Unit) {
        statusBuilder = block
    }

    fun login(block: ProtocolBuilder.() -> Unit) {
        loginBuilder = block
    }

    fun game(block: ProtocolBuilder.() -> Unit) {
        gameBuilder = block
    }

    inner class ProtocolBuilder internal constructor() {
        fun clientbound(id: Int, codec: PacketCodec<out MinecraftPacket>) {
            if (isServer) {
                registerOutgoing(id, codec)
            } else {
                registerIncoming(id, codec)
            }
        }

        fun serverbound(id: Int, codec: PacketCodec<out MinecraftPacket>) {
            if (isServer) {
                registerIncoming(id, codec)
            } else {
                registerOutgoing(id, codec)
            }
        }
    }
}

fun MinecraftProtocol(
    version: MinecraftVersion,
    isServer: Boolean,
    protocol: MinecraftProtocol.() -> Unit
): () -> MinecraftProtocol = {
    object : MinecraftProtocol(version, isServer) {
        init {
            apply(protocol)
        }
    }
}