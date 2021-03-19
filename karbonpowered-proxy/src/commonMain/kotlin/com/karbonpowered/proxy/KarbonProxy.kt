package com.karbonpowered.proxy

import com.karbonpowered.network.NetworkServer
import com.karbonpowered.network.Session
import com.karbonpowered.protocol.HandshakeProtocol
import io.ktor.network.sockets.*

class KarbonProxy : NetworkServer() {
    init {

    }

    override fun newSession(connection: Connection): Session = ProxySession(
        connection, HandshakeProtocol
    )

    override fun sessionInactivated(session: Session) {

    }
}