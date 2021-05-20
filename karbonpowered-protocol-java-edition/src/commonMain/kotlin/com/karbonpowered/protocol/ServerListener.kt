package com.karbonpowered.protocol

import com.karbonpowered.api.network.status.StatusResponse
import com.karbonpowered.common.UUID
import com.karbonpowered.common.md5
import com.karbonpowered.common.uuidOf
import com.karbonpowered.core.network.status.StatusResponse
import com.karbonpowered.profile.GameProfile
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundGameJoinPacket
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundKeepAlivePacket
import com.karbonpowered.protocol.packet.clientbound.login.ClientboundLoginSuccessPacket
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusPongPacket
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusResponsePacket
import com.karbonpowered.protocol.packet.serverbound.handshake.ServerboundHandshakePacket
import com.karbonpowered.protocol.packet.serverbound.login.ServerboundLoginStartPacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusPingPacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusRequestPacket
import com.karbonpowered.server.Session
import com.karbonpowered.server.event.PacketReceivedEvent
import com.karbonpowered.server.event.PacketSentEvent
import com.karbonpowered.server.event.SessionListener
import io.ktor.utils.io.core.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.random.Random

open class ServerListener : SessionListener {
    private val verifyToken = Random.Default.nextBytes(4)

    var ping = 0
    var lastPingTime = 0L
    var lastPingId = 0
    var username = ""
    var serverInfoBuilder: StatusResponse.Builder.(Session) -> Unit = {}
    lateinit var profile: GameProfile

    override fun packetReceived(event: PacketReceivedEvent) {
        val protocol = event.session.packetProtocol as MinecraftProtocol
        val packet = event.packet
        val session = event.session
        println("RECEIVED: $packet")
        when (packet) {
            is ServerboundHandshakePacket -> {
                when (packet.handshakeIntent) {
                    MinecraftProtocol.SubProtocol.STATUS -> {
                        protocol.subProtocol = MinecraftProtocol.SubProtocol.STATUS
                    }
                    MinecraftProtocol.SubProtocol.LOGIN -> {
                        protocol.subProtocol = MinecraftProtocol.SubProtocol.LOGIN
                        if (packet.protocolVersion > protocol.version.protocol) {
                            session.disconnect("Outdated server! I'm still on ${protocol.version.name}")
                        } else if (packet.protocolVersion < protocol.version.protocol) {
                            session.disconnect("Outdated client! Please use ${protocol.version.name}")
                        }
                    }
                    else -> throw UnsupportedOperationException("Invalid client intent: ${packet.handshakeIntent}")
                }
            }
            is ServerboundLoginStartPacket -> {
                username = packet.username
                session.launch {
                    profile = userAuth()
                    session.sendPacket(ClientboundLoginSuccessPacket(profile.uniqueId, username))
                }
            }
            is ServerboundStatusRequestPacket -> {
                val response = StatusResponse {
                    serverInfoBuilder(this, session)
                }
                val responsePacket = ClientboundStatusResponsePacket(response)
                session.sendPacket(responsePacket)
            }
            is ServerboundStatusPingPacket -> {
                session.sendPacket(ClientboundStatusPongPacket(packet.payload))
            }
        }
    }

    override fun packetSent(event: PacketSentEvent) {
        val session = event.session
        when (event.packet) {
            is ClientboundLoginSuccessPacket -> {
                (session.packetProtocol as MinecraftProtocol).subProtocol = MinecraftProtocol.SubProtocol.GAME
            }
            is ClientboundGameJoinPacket -> {
                keepAliveJob(session)
            }
        }
    }

    fun keepAliveJob(session: Session) = session.launch {
        while (session.isConnected) {
            session.sendPacket(ClientboundKeepAlivePacket(0))
            delay(2000)
        }
    }

    suspend fun userAuth(key: ByteArray? = null): GameProfile = suspendCoroutine { continuation ->
        val profile: GameProfile
        if (key != null) {
            TODO()
        } else {
            profile = GameProfile(offlineUuid(username), username)
        }
        this@ServerListener.profile = profile
        continuation.resume(profile)
    }

    companion object {
        fun offlineUuid(username: String): UUID {
            val bytes = "OfflinePlayer:$username".toByteArray()
            val md5Bytes = bytes.md5()
            md5Bytes[6] = md5Bytes[6] and 0x0f
            md5Bytes[6] = md5Bytes[6] or 0x30
            md5Bytes[8] = md5Bytes[8] and 0x3f
            md5Bytes[8] = md5Bytes[8] or 0x80.toByte()
            return uuidOf(md5Bytes)
        }
    }
}