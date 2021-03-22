package com.karbonpowered.engine.network.handler

import com.karbonpowered.network.MessageHandler
import com.karbonpowered.network.Session
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusResponsePacket
import com.karbonpowered.protocol.packet.serverbound.status.ServerboundStatusRequestPacket

object StatusRequestHandler : MessageHandler<Session, ServerboundStatusRequestPacket> {
    private val TEST_RESPONSE = """
        {
            "version": {
                "name": "1.16.5",
                "protocol": 754
            },
            "players": {
                "max": 100,
                "online": 5,
                "sample": [
                ]
            },
            "description": {
                "text": "Hello world"
            },
            "favicon": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABABAMAAABYR2ztAAAALVBMVEUAAABAQEABAQEFBQUDAwMKCgoaGhoFBQUfHx8ODg4tLS0NDQ0CAgIDAwMAAABWvQxFAAAADnRSTlMACO6zlE03u2FfHH3czrOdCtYAAADZSURBVEjH1dQhCgJBGIbhtRhMK9g8gcUqbBSzQbB7C69g9AAGT+JBjG5Vz+C6CD/Mw4xVv/zwMhNmqj/ZYJvdrgfD+TOz2/ELmFUAAgADAQwEeBySnfpAgLZObr5+BwpgEoEABAAEAAQABAAEAhAAEAAQABAAEAAQAEQAQABAAGBAEAEBAYEBwaYD1xKYdqA9l85w6URTACQAkRCQAJAAkACQAJAAkABEAkACQAJAAkACQAJAAkACYCKAiQD3VbJlJAqfeVMEfUJgQmBitNjn9nmn4+zq6if2AggG6uMG99pWAAAAAElFTkSuQmCC"
        }
    """.trimIndent()

    override suspend fun handle(session: Session, message: ServerboundStatusRequestPacket) {
        session.send(ClientboundStatusResponsePacket(TEST_RESPONSE))
    }
}