package com.karbonpowered.engine.player

import com.karbonpowered.common.Identifiable
import com.karbonpowered.common.UUID
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.entity.KarbonEntity
import com.karbonpowered.engine.scheduler.AsyncManager
import com.karbonpowered.engine.world.discrete.NullTransformProvider
import com.karbonpowered.engine.world.discrete.TransformProvider
import com.karbonpowered.server.Session

open class KarbonPlayer(
    val engine: KarbonEngine,
    override val uniqueId: UUID,
    val username: String,
    session: Session,
) : Identifiable, AsyncManager {
    open val network = PlayerNetwork(this, session)

    fun isInvisible(observed: KarbonEntity): Boolean = false

    var transformProvider: TransformProvider = NullTransformProvider

    override suspend fun preSnapshotRun() {
        network.preSnapshotRun()
    }
}