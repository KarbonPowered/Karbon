package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundUnloadColumnPacket(
    val chunkX: Int,
    val chunkZ: Int
) : MinecraftPacket {
    object Codec : PacketCodec<ClientboundUnloadColumnPacket> {
        override val packetType: KClass<ClientboundUnloadColumnPacket>
            get() = ClientboundUnloadColumnPacket::class

        override fun encode(output: Output, packet: ClientboundUnloadColumnPacket) {
            output.writeInt(packet.chunkX)
            output.writeInt(packet.chunkZ)
        }

        override fun decode(input: Input): ClientboundUnloadColumnPacket {
            val chunkX = input.readInt()
            val chunkZ = input.readInt()
            return ClientboundUnloadColumnPacket(chunkX, chunkZ)
        }
    }
}