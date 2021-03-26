package com.karbonpowered.engine.component

import com.karbonpowered.component.entity.PlayerNetworkComponent
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.engine.network.KarbonSession
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundGamePlayerPositionRotationPacket

class KarbonPlayerNetworkComponent(
    val session: KarbonSession
) : PlayerNetworkComponent() {

    override suspend fun sendPositionUpdates(position: DoubleVector3, rotation: DoubleVector3) {
        owner as KarbonPlayer
        session.send(ClientboundGamePlayerPositionRotationPacket(position, rotation))
    }
}