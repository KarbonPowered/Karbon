package com.karbonpowered.engine.entity

import com.karbonpowered.common.UUID
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.component.PlayerNetworkComponent
import com.karbonpowered.server.Session

class KarbonPlayer(
    engine: KarbonEngine,
    val uniqueId: UUID,
    val username: String,
    session: Session
) : KarbonEntity(engine) {
    init {
        components.add(PlayerNetworkComponent(session))
    }

    val network get() = components[PlayerNetworkComponent::class]
}