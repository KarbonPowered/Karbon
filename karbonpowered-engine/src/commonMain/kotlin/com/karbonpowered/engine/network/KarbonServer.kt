package com.karbonpowered.engine.network

import com.karbonpowered.api.profile.GameProfile
import com.karbonpowered.api.world.server.ServerLocation
import com.karbonpowered.api.world.server.ServerWorld
import com.karbonpowered.engine.component.KarbonPlayerNetworkComponent
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.engine.world.KarbonWorld
import com.karbonpowered.math.vector.BaseMutableDoubleVector3
import com.karbonpowered.math.vector.MutableDoubleVector3
import com.karbonpowered.network.NetworkServer
import com.karbonpowered.network.Session
import io.ktor.network.sockets.*

class KarbonServer : NetworkServer() {
    override fun newSession(connection: Connection): Session = KarbonSession(
        connection, HandshakeProtocol(true)
    )

    override fun sessionInactivated(session: Session) {

    }

    val world = KarbonWorld()

    fun addPlayer(gameProfile: GameProfile, session: KarbonSession) {
        val network = KarbonPlayerNetworkComponent()
        val player = KarbonPlayer(gameProfile, world, object : ServerLocation, BaseMutableDoubleVector3() {
            override val world: ServerWorld = this@KarbonServer.world
        })

    }


}