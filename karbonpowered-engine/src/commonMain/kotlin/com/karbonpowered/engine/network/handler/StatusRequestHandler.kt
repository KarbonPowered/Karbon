package com.karbonpowered.engine.network.handler

import com.karbonpowered.engine.Engine
import com.karbonpowered.network.MessageHandler
import com.karbonpowered.network.Session
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusResponsePacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusRequestPacket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object StatusRequestHandler : MessageHandler<Session, ServerboundStatusRequestPacket> {
    private const val FAVICON =
            "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABABAMAAABYR2ztAAAALVBMVEUAAABAQEABAQEFBQUDAwMKCgoaGhoFBQUfHx8ODg4tLS0NDQ0CAgIDAwMAAABWvQxFAAAADnRSTlMACO6zlE03u2FfHH3czrOdCtYAAADZSURBVEjH1dQhCgJBGIbhtRhMK9g8gcUqbBSzQbB7C69g9AAGT+JBjG5Vz+C6CD/Mw4xVv/zwMhNmqj/ZYJvdrgfD+TOz2/ELmFUAAgADAQwEeBySnfpAgLZObr5+BwpgEoEABAAEAAQABAAEAhAAEAAQABAAEAAQAEQAQABAAGBAEAEBAYEBwaYD1xKYdqA9l85w6URTACQAkRCQAJAAkACQAJAAkABEAkACQAJAAkACQAJAAkACYCKAiQD3VbJlJAqfeVMEfUJgQmBitNjn9nmn4+zq6if2AggG6uMG99pWAAAAAElFTkSuQmCC"
    private const val PROTOCOL_VERSION = 0x40000000 + 20
    private const val PROTOCOL_NAME = "KarbonPowered 21w13a"
    private const val MOTD = "KarbonPowered Demo Server"

    override fun handle(session: Session, message: ServerboundStatusRequestPacket) {
        GlobalScope.launch {
            val response = createResponse()
            val packet = ClientboundStatusResponsePacket(response)
            session.send(packet)
        }
    }

    private fun createResponse() = """
        {
            "version": {
                "name": "$PROTOCOL_NAME",
                "protocol": $PROTOCOL_VERSION
            },
            "players": {
                "max": ${Engine.server.maxPlayers},
                "online": ${Engine.server.players.size},
                "sample": []
            },
            "description": {
                "text": "$MOTD"
            },
            "favicon": "$FAVICON"
        }
    """.trimIndent()
}