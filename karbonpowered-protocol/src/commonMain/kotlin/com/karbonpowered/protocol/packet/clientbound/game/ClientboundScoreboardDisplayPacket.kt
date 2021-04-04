package com.karbonpowered.protocol.packet.clientbound.game

import com.karbonpowered.network.MessageCodec
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.protocol.readString
import com.karbonpowered.protocol.readVarInt
import com.karbonpowered.protocol.writeString
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundScoreboardDisplayPacket(
    val slot: Int,
    val name: String
) : MinecraftPacket {
    companion object : MessageCodec<ClientboundScoreboardDisplayPacket> {
        override val messageType = ClientboundScoreboardDisplayPacket::class

        override fun decode(input: Input): ClientboundScoreboardDisplayPacket {
            val slot = input.readByte().toInt()
            val name = input.readString()
            return ClientboundScoreboardDisplayPacket(slot, name)
        }

        override fun encode(output: Output, data: ClientboundScoreboardDisplayPacket) {
            output.writeByte(data.slot.toByte())
            output.writeString(data.name)
        }
    }
}