package com.karbonpowered.engine.entity

import com.karbonpowered.common.UUID
import com.karbonpowered.engine.scheduler.AsyncManager
import com.karbonpowered.engine.snapshot.SnapshotManager
import com.karbonpowered.engine.snapshot.SnapshotableHashMap
import com.karbonpowered.engine.world.KarbonRegion

class EntityManager(
    val region: KarbonRegion
) : AsyncManager {
    val snapshotManager = SnapshotManager()
    val entities = SnapshotableHashMap<UUID, KarbonEntity>(snapshotManager)

    fun addEntity(entity: KarbonEntity) {
        entities[entity.uniqueId] = entity
    }

    fun removeEntity(uniqueId: UUID) = entities.remove(uniqueId)

    fun removeEntity(entity: KarbonEntity) = removeEntity(entity.uniqueId)

    fun copyAllSnapshots() {
        entities.values.forEach {
            it.copySnapshot()
        }
        snapshotManager.copyAllSnapshots()

        entities.values.forEach { entity ->
            if (entity.isRemoved) {
                removeEntity(entity)
            }
        }
    }

    override suspend fun preSnapshotRun() {
        entities.values.forEach { entity ->
            entity.preSnapshotRun()
        }
    }

    override suspend fun finalizeRun() {
        entities.values.forEach { entity ->
            entity.finalizeRun()
        }
    }
}