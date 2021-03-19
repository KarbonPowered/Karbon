package com.karbonpowered.engine.network.handler

import com.karbonpowered.engine.network.StatusProtocol
import com.karbonpowered.network.MessageHandler
import com.karbonpowered.network.Session
import com.karbonpowered.protocol.packet.clientbound.status.ClientboundStatusResponsePacket
import com.karbonpowered.protocol.packet.serverbound.handshake.ServerboundHandshakePacket

object HandshakeHandler : MessageHandler<Session, ServerboundHandshakePacket> {
    override suspend fun handle(session: Session, message: ServerboundHandshakePacket) {
        if (message.nextState == 1) {
            session.protocol = StatusProtocol(true)
            session.send(
                ClientboundStatusResponsePacket(
                    """
            {
                "version": {
                    "name": "1.16.5",
                    "protocol": 754
                },
                "players": {
                    "max": 100,
                    "online": 5,
                    "sample": [
                        {
                            "name": "XjCyan1de",
                            "id": "52fba512-2361-4957-8fb6-c5899e3a8db8"
                        }
                    ]
                },
                "description": {
                    "text": "Hello world\nKarbonPowered"
                },
                "favicon": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABABAMAAABYR2ztAAAALVBMVEUAAABAQEABAQEFBQUDAwMKCgoaGhoFBQUfHx8ODg4tLS0NDQ0CAgIDAwMAAABWvQxFAAAADnRSTlMACO6zlE03u2FfHH3czrOdCtYAAADZSURBVEjH1dQhCgJBGIbhtRhMK9g8gcUqbBSzQbB7C69g9AAGT+JBjG5Vz+C6CD/Mw4xVv/zwMhNmqj/ZYJvdrgfD+TOz2/ELmFUAAgADAQwEeBySnfpAgLZObr5+BwpgEoEABAAEAAQABAAEAhAAEAAQABAAEAAQAEQAQABAAGBAEAEBAYEBwaYD1xKYdqA9l85w6URTACQAkRCQAJAAkACQAJAAkABEAkACQAJAAkACQAJAAkACYCKAiQD3VbJlJAqfeVMEfUJgQmBitNjn9nmn4+zq6if2AggG6uMG99pWAAAAAElFTkSuQmCC"
            }
        """.trimIndent()
                )
            )
        }
    }
}