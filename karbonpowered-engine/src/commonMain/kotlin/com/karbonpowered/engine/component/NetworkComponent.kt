package com.karbonpowered.engine.component

import com.karbonpowered.engine.entity.KarbonEntity
import com.karbonpowered.engine.snapshot.Snapshotable
import com.karbonpowered.engine.util.Transform

open class NetworkComponent(entity: KarbonEntity) : EntityComponent(entity), Snapshotable {
    override fun copySnapshot() {

    }

    fun preSnapshotRun(transform: Transform) {

    }
}