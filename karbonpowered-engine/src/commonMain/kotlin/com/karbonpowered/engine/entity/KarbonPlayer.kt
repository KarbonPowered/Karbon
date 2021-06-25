package com.karbonpowered.engine.entity

import com.karbonpowered.common.UUID
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.component.PlayerNetworkComponent
import com.karbonpowered.engine.util.Transform
import com.karbonpowered.server.Session

open class KarbonPlayer(
    engine: KarbonEngine,
    override val uniqueId: UUID,
    val username: String,
    val session: Session,
    transform: Transform? = null
) : KarbonEntity(engine, transform) {
    fun isInvisible(observed: KarbonEntity): Boolean = false

    override val network get() = components[PlayerNetworkComponent::class, { PlayerNetworkComponent(this, session) }]
}