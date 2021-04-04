package com.karbonpowered.engine.component

import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.component.entity.NetworkComponent
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
) : NetworkComponent() {
    private var sync: Boolean = false
    var lastKeepAlive = TimeSource.Monotonic.markNow()

    override fun canTick(): Boolean = true

    override fun onAttached() {
        check(owner is Player) { "PlayerNetworkComponent may only be attach to player" }
        super.onAttached()
    }

    suspend fun sendPositionUpdates(position: DoubleVector3, rotation: DoubleVector3) {
        if ((owner as KarbonPlayer).physics.isTransformDirty() && sync) {
            session.send(ClientboundGamePlayerPositionRotationPacket(position, rotation))
            sync = false
        }
    }

    fun forceSync() {
        sync = true
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun onTick(duration: Duration) {
        val keepAliveDuration = lastKeepAlive.elapsedNow()
        if (keepAliveDuration > 15.seconds) {
            session.send(ClientboundKeepAlivePacket(keepAliveDuration.inMilliseconds.toLong()))
            lastKeepAlive = TimeSource.Monotonic.markNow()
        }
    }
}