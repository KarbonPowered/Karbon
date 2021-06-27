package com.karbonpowered.engine.entity

import com.karbonpowered.common.Identifiable
import com.karbonpowered.common.UUID
import com.karbonpowered.engine.world.discrete.NullTransformProvider
import com.karbonpowered.engine.world.discrete.TransformProvider
import com.karbonpowered.server.Session

open class KarbonPlayer(
    override val uniqueId: UUID,
    val username: String,
    val session: Session,
) : Identifiable {
    fun isInvisible(observed: KarbonEntity): Boolean = false

    var transformProvider: TransformProvider = NullTransformProvider
}