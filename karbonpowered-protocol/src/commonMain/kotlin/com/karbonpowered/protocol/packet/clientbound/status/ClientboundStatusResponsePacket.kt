package com.karbonpowered.protocol.packet.clientbound.status

import com.karbonpowered.api.network.status.StatusResponse
import com.karbonpowered.protocol.MinecraftPacket
import com.karbonpowered.server.packet.PacketCodec
import com.karbonpowered.server.readString
import com.karbonpowered.server.writeString
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

data class ClientboundStatusResponsePacket(
    val response: StatusResponse
) : MinecraftPacket {
    companion object : PacketCodec<ClientboundStatusResponsePacket> {
        override val packetType: KClass<ClientboundStatusResponsePacket>
            get() = ClientboundStatusResponsePacket::class

        override fun decode(input: Input): ClientboundStatusResponsePacket {
            val rawResponse = input.readString()
            TODO(rawResponse)
        }

        override fun encode(output: Output, packet: ClientboundStatusResponsePacket) {
            val rawResponse = buildString {
                append("{")
                append("\"version\":{")
                append("\"name\":\"")
                append(packet.response.version.name)
                append("\",\"protocol\":")
                append(packet.response.version.protocolVersion)
                append("},")
                val players = packet.response.players
                if (players != null) {
                    append("\"players\":{")
                    append("\"max\":")
                    append(players.max)
                    append(",\"online\":")
                    append(players.online)
                    append(",")
                    append("\"sample\":[")
                    players.profiles.forEachIndexed { index, profile ->
                        append("{\"name\":\"")
                        append(profile.name)
                        append("\",\"id\":\"")
                        append(profile.uniqueId.toString())
                        append("\"}")
                        if (index != players.profiles.lastIndex) {
                            append(',')
                        }
                    }
                    append("]},")
                }
                append("\"description\":")
                append(packet.response.description)
                val favicon = packet.response.favicon
                if (favicon != null) {
                    append(",\"favicon\":\"")
                    append(favicon)
                    append("\"")
                }
                append("}")
            }
            output.writeString(rawResponse)
        }
    }
}