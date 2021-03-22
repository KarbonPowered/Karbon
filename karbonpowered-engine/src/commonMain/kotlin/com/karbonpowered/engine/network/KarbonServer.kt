package com.karbonpowered.engine.network

import com.karbonpowered.network.NetworkServer
import com.karbonpowered.network.Session
import io.ktor.network.sockets.*

class KarbonServer : NetworkServer() {
    override fun newSession(connection: Connection): Session = KarbonSession(
        connection, HandshakeProtocol(true)
    )

    override fun sessionInactivated(session: Session) {

    }
}