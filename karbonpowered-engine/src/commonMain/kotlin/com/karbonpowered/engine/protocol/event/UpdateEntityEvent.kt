package com.karbonpowered.engine.protocol.event

import com.karbonpowered.common.UUID
import com.karbonpowered.engine.protocol.ProtocolEvent
import com.karbonpowered.engine.world.discrete.Transform
import com.karbonpowered.server.Session

data class UpdateEntityEvent(
    override val session: Session,
    val uniqueId: UUID,
    val transform: Transform,
    val action: UpdateAction
) : ProtocolEvent {
    enum class UpdateAction {
        ADD,
        REMOVE,
        TRANSFORM
    }
}