package com.karbonpowered.engine.entity

import com.karbonpowered.common.UUID
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.component.PlayerNetworkComponent
import com.karbonpowered.server.Session

class KarbonPlayer(
    engine: KarbonEngine,
    override val uniqueId: UUID,
    val username: String,
    session: Session
) : KarbonEntity(engine) {
    fun isInvisible(observed: KarbonEntity): Boolean = false

    init {
        components.add(PlayerNetworkComponent(this, session))
    }

    override val network get() = components[PlayerNetworkComponent::class]
}