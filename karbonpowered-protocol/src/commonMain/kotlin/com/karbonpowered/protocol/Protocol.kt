package com.karbonpowered.protocol

import com.karbonpowered.network.Codec
import com.karbonpowered.protocol.clientbound.status.ClientboundStatusPong
import com.karbonpowered.protocol.clientbound.status.ClientboundStatusResponse
import com.karbonpowered.protocol.serverbound.handshake.ServerboundHandshakePacket
import com.karbonpowered.protocol.serverbound.login.ServerboundLoginStartPacket
import com.karbonpowered.protocol.serverbound.status.ServerboundStatusPing
import com.karbonpowered.protocol.serverbound.status.ServerboundStatusRequest
import kotlin.reflect.KClass

open class Protocol {
    private val byOpcode = mutableMapOf<Int, RegistryEntry<*>>()
    private val byKlass = mutableMapOf<KClass<*>, RegistryEntry<*>>()

    fun <T : MinecraftPacket> register(
        opcode: Int,
        klass: KClass<T>,
        codec: Codec<T>,
    ) {
        val registryEntry = RegistryEntry(opcode, klass, codec)
        byOpcode[opcode] = registryEntry
        byKlass[klass] = registryEntry
    }

    operator fun get(opcode: Int): RegistryEntry<*>? = byOpcode[opcode]

    @Suppress("UNCHECKED_CAST")
    operator fun <T : MinecraftPacket> get(klass: KClass<T>): RegistryEntry<T>? = byKlass[klass] as? RegistryEntry<T>

    data class RegistryEntry<T : MinecraftPacket>(
        val opcode: Int,
        val klass: KClass<T>,
        val codec: Codec<T>,
    )
}

object HandshakeProtocol : MinecraftProtocol("handshake") {
    init {
        bind(0x00, ServerboundHandshakePacket::class, ServerboundHandshakePacket)
    }
}

object ServerboundLoginProtocol : MinecraftProtocol("server-login") {
    init {
        bind(0x00, ServerboundLoginStartPacket::class, ServerboundLoginStartPacket)
    }
}

object ClientboundStatusProtocol : MinecraftProtocol("client-status") {
    init {
        bind(0x00, ClientboundStatusResponse::class, ClientboundStatusResponse)
        HandshakeProtocol.bind(0x01, ClientboundStatusPong::class, ClientboundStatusPong)
    }
}

object ServerboundStatusRequest : MinecraftProtocol("server-status") {
    init {
        bind(0x00, ServerboundStatusRequest::class, ServerboundStatusRequest)
        bind(0x01, ServerboundStatusPing::class, ServerboundStatusPing)
    }
}