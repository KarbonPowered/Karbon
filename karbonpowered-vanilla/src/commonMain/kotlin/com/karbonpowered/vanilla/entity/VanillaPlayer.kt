package com.karbonpowered.vanilla.entity

import com.karbonpowered.common.UUID
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.engine.util.Transform
import com.karbonpowered.server.Session
import com.karbonpowered.vanilla.component.VanillaPlayerNetworkComponent

class VanillaPlayer(
    engine: KarbonEngine,
    uniqueId: UUID,
    username: String,
    session: Session,
    spawnPoint: Transform
) : KarbonPlayer(engine, uniqueId, username, session, spawnPoint) {
    override val network: VanillaPlayerNetworkComponent
        get() = components[VanillaPlayerNetworkComponent::class, {
            VanillaPlayerNetworkComponent(
                this,
                session
            )
        }]
}