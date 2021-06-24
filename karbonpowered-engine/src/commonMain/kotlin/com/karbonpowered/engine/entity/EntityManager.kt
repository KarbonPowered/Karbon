package com.karbonpowered.engine.entity

import com.karbonpowered.common.UUID
import com.karbonpowered.engine.scheduler.AsyncManager
import com.karbonpowered.engine.snapshot.SnapshotManager
import com.karbonpowered.engine.snapshot.SnapshotableHashMap
import com.karbonpowered.engine.world.KarbonRegion
import com.karbonpowered.engine.world.LoadOption
import com.karbonpowered.math.vector.distanceSquared
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class EntityManager(
    val region: KarbonRegion
) : AsyncManager {
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

    suspend fun syncEntities() = coroutineScope {
        val entities = entities.values.asSequence()
        val players = entities.filterIsInstance<KarbonPlayer>()
        entities.forEach { observed ->
            launch {
                val chunk = observed.physics.transform.position.chunk(LoadOption.NO_LOAD) ?: return@launch
                val observers = players.filter { it.network.isObservedChunk(chunk) }
                syncEntity(observed, observers, false)
            }
        }
    }

    fun syncEntity(observed: KarbonEntity, observers: Sequence<KarbonPlayer>, forceDestroy: Boolean) {
        observers.forEach { player ->
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
                    println("Remove entity: $observed for $player")
                } else {
                    // TRANSFORM
                    println("Transform entity: $observed for $player")
                }
            } else if (!tooFar && !isInvisible) {
                // ADD
                println("Add entity: $observed for $player")
            } else {
                return
            }
        }
    }

    override suspend fun preSnapshotRun() {
        entities.values.forEach { entity ->
            entity.preSnapshotRun()
        }
    }
}