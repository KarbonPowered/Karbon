package com.karbonpowered.engine.entity

import com.karbonpowered.common.UUID
import com.karbonpowered.engine.snapshot.SnapshotManager
import com.karbonpowered.engine.snapshot.SnapshotableHashMap
import com.karbonpowered.engine.world.KarbonRegion
import com.karbonpowered.math.vector.distanceSquared

class EntityManager(
    val region: KarbonRegion
) {
    val snapshotManager = SnapshotManager()
    val entities = SnapshotableHashMap<UUID, KarbonEntity>(snapshotManager)

    fun addEntity(entity: KarbonEntity) {
        entities[entity.uniqueId] = entity
    }

    fun removeEntity(entity: KarbonEntity) {
        entities.remove(entity.uniqueId)
    }

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

    fun syncEntity(observed: KarbonEntity, observers: Iterable<KarbonEntity>, forceDestroy: Boolean) {
        observers.forEach { observer ->
            val player = observer as? KarbonPlayer ?: return
            val network = player.network
            val syncDistance = network.syncDistance
            val physics = observed.physics
            val hasSpawned = network.hasSpawned(observed)
            val isRemoved = observed.isRemoved
            val distance = physics.transformLive.position distanceSquared player.physics.transformLive.position
            //Entity is out of range of the player's view distance, destroy
            val tooFar = distance > syncDistance * syncDistance
            val isInvisible = player.isInvisible(observed)
            if (hasSpawned) {
                if (forceDestroy || isRemoved || tooFar || isInvisible) {
                    // REMOVE
                } else {
                    // TRANSFORM
                }
            } else if (!tooFar && !isInvisible) {
                // ADD
            } else {
                return
            }
        }
    }
}