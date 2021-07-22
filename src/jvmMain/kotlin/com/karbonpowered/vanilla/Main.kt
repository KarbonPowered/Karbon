package com.karbonpowered.vanilla

import com.karbonpowered.vanilla.network.VanillaProtocol
import com.karbonpowered.vanilla.network.protocol.clientbound.ClientboundStatusPongPacket
import com.karbonpowered.vanilla.network.protocol.clientbound.ClientboundStatusResponsePacket
import com.karbonpowered.vanilla.network.protocol.serverbound.ServerboundHandshakePacket
import com.karbonpowered.vanilla.network.protocol.serverbound.ServerboundStatusPingPacket
import com.karbonpowered.vanilla.network.protocol.serverbound.ServerboundStatusRequestPacket
import kotlinx.coroutines.runBlocking

val motd = """
        {
            "version": {
                "name": "1.17.1",
                "protocol": 756
            },
            "players": {
                "max": 20,
                "online": 0,
                "sample": []
            },
            "description": {
                "text": "A KarbonPowered Server"
            },
            "favicon": "data:image/png;base64,<data>"
        }
    """.trimIndent()

fun main() = runBlocking {

    vanillaServer(
        inbound = { packet ->
            when (packet) {
                is ServerboundHandshakePacket -> {
                    context.protocol.state = VanillaProtocol.ProtocolState[packet.nextState]
                }
                is ServerboundStatusRequestPacket -> {
                    context.send(ClientboundStatusResponsePacket(motd))
                }
                is ServerboundStatusPingPacket -> context.send(ClientboundStatusPongPacket(packet.payload))
            }
            println("receive packet: $packet")
        }
    )
}