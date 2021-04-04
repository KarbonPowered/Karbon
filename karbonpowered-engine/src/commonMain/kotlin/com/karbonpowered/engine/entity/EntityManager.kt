package com.karbonpowered.engine.entity

import com.karbonpowered.engine.scheduler.TickManager
import com.karbonpowered.engine.util.concurrent.snapshotable.SnapshotManager
import com.karbonpowered.engine.util.concurrent.snapshotable.SnapshotableArrayList
import kotlinx.atomicfu.atomic
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class EntityManager : TickManager {
    val snapshotManager = SnapshotManager()

    var nextId = atomic(1)
    val entities = SnapshotableArrayList<KarbonEntity<*>>(snapshotManager)

    fun nextId() = nextId.getAndIncrement()

    fun addEntity(entity: KarbonEntity<*>) {
        var currentId = entity.id
        if (currentId == Int.MIN_VALUE) {
            currentId = nextId()
            entity.id = currentId
        }
        entities.add(entity)
    }

    fun removeEntity(entity: KarbonEntity<*>) {
        entities.remove(entity)
    }

    override fun startTickRun(duration: Duration) {
        entities.forEach {
            it.startTickRun(duration)
        }
    }

    fun copyAllSnapshots() {
        entities.forEach {
            it.copySnapshot()
        }
        snapshotManager.copyAllSnapshots()
        entities.forEach {
            if (it.isRemoved()) {
                removeEntity(it)
            }
        }
    }

//    fun syncEntities() {
//        entities.forEach { observed ->
//            if (observed.id == Int.MIN_VALUE) {
//                throw IllegalStateException("Attempt to sync entity with not spawned id")
//            }
//        }
//    }
}