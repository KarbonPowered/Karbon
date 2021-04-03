package com.karbonpowered.engine.component

import com.karbonpowered.component.entity.PlayerNetworkComponent
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.engine.network.KarbonSession
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundGamePlayerPositionRotationPacket
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundKeepAlivePacket
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import kotlin.time.seconds

@OptIn(ExperimentalTime::class)
class KarbonPlayerNetworkComponent(
    val session: KarbonSession
) : PlayerNetworkComponent() {
    override fun canTick(): Boolean = true
    var lastKeepAlive = TimeSource.Monotonic.markNow()

    override suspend fun sendPositionUpdates(position: DoubleVector3, rotation: DoubleVector3) {
        owner as KarbonPlayer
        session.send(ClientboundGamePlayerPositionRotationPacket(position, rotation))
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun onTick(duration: Duration) {
        val keepAliveDuration = lastKeepAlive.elapsedNow()
        if (keepAliveDuration > 15.seconds) {
            session.send(ClientboundKeepAlivePacket(keepAliveDuration.inMilliseconds.toLong()))
            lastKeepAlive = TimeSource.Monotonic.markNow()
            println("tick network = $keepAliveDuration")
        }
    }
}