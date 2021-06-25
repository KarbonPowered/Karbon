package com.karbonpowered.vanilla.component

import com.karbonpowered.core.entity.living.player.GameModes
import com.karbonpowered.engine.component.PlayerNetworkComponent
import com.karbonpowered.engine.protocol.ProtocolEvent
import com.karbonpowered.engine.protocol.ProtocolEventListener
import com.karbonpowered.engine.protocol.event.WorldChangeProtocolEvent
import com.karbonpowered.engine.world.KarbonWorld
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundGameJoinPacket
import com.karbonpowered.protocol.packet.clientbound.game.ClientboundGamePlayerPositionRotationPacket
import com.karbonpowered.server.Session
import com.karbonpowered.vanilla.VanillaServer
import com.karbonpowered.vanilla.entity.VanillaPlayer
import com.karbonpowered.vanilla.world.VanillaWorld

class VanillaPlayerNetworkComponent(
    player: VanillaPlayer,
    session: Session
) : PlayerNetworkComponent(player, session), ProtocolEventListener {
    override fun onProtocolEvent(event: ProtocolEvent) {
        when (event) {
            is WorldChangeProtocolEvent -> worldChanged(event.world)
        }
    }

    fun worldChanged(world: KarbonWorld) {
        world as VanillaWorld
        session.sendPackets(
            ClientboundGameJoinPacket(
                0, false, GameModes.CREATIVE, GameModes.NOT_SET, (engine as VanillaServer).worlds.keys.toList(),
                world.createDimensionCodec(),
                world.createOverworldTag(),
                world.name,
                world.seed,
                100,
                syncDistance,
                false,
                false,
                false,
                true
            ),
            ClientboundGamePlayerPositionRotationPacket()
        )
    }

    override fun onAttached() {
        session.addListener(this)
    }
}