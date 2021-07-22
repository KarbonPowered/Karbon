package com.karbonpowered.engine.protocol.event

import com.karbonpowered.engine.protocol.ProtocolEvent
import com.karbonpowered.engine.world.KarbonWorld
import com.karbonpowered.server.Session

data class WorldChangeProtocolEvent(
    override val session: Session,
    val world: KarbonWorld
) : ProtocolEvent