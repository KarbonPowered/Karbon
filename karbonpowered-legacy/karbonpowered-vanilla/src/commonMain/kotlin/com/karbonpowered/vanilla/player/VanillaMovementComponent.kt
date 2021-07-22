package com.karbonpowered.vanilla.player

import com.karbonpowered.engine.component.Component
import com.karbonpowered.engine.entity.KarbonEntity
import com.karbonpowered.engine.world.discrete.Position
import com.karbonpowered.math.imaginary.FloatQuaternion
import com.karbonpowered.protocol.packet.serverbound.game.ServerboundPlayerPositionPacket
import com.karbonpowered.protocol.packet.serverbound.game.ServerboundPlayerPositionRotationPacket
import com.karbonpowered.server.event.PacketReceivedEvent
import com.karbonpowered.server.event.SessionListener
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class VanillaMovementComponent(
    val player: VanillaPlayer,
    val entity: KarbonEntity
) : Component(), SessionListener {
    @OptIn(ExperimentalTime::class)
    override suspend fun tick(duration: Duration) {
    }

    override fun packetReceived(event: PacketReceivedEvent) {
        val packet = event.packet
        if (packet is ServerboundPlayerPositionPacket) {
            val currentTransform = entity.physics.transform
            entity.physics.transform = currentTransform.copy(
                position = Position(entity.world, packet.x.toFloat(), packet.y.toFloat(), packet.z.toFloat())
            )
        } else if (packet is ServerboundPlayerPositionRotationPacket) {
            val currentTransform = entity.physics.transform
            entity.physics.transform = currentTransform.copy(
                position = Position(entity.world, packet.x.toFloat(), packet.y.toFloat(), packet.z.toFloat()),
                rotation = FloatQuaternion.fromAxesAnglesRad(packet.pitch, packet.yaw, 0f)
            )
        }
    }

    override fun onAttached() {
        player.network.session.addListener(this)
    }
}