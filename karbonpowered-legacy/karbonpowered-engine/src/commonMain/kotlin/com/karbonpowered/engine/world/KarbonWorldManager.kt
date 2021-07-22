package com.karbonpowered.engine.world

import com.karbonpowered.common.UUID
import com.karbonpowered.data.ResourceKey
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.world.generator.EmptyWorldGenerator
import com.karbonpowered.engine.world.generator.WorldGenerator
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

class KarbonWorldManager(
    val engine: KarbonEngine
) {
    private val loadedWorlds = HashMap<ResourceKey, KarbonWorld>()
    private val lock = reentrantLock()

    val worlds: Collection<KarbonWorld> get() = lock.withLock { loadedWorlds.values.toList() }
    val defaultWorld: KarbonWorld get() = worlds.first()

    fun world(identifier: ResourceKey): KarbonWorld? = lock.withLock {
        loadedWorlds[identifier]
    }

    fun world(uniqueId: UUID): KarbonWorld? = lock.withLock {
        loadedWorlds.values.find { it.uniqueId == uniqueId }
    }

    suspend fun loadWorld(identifier: ResourceKey, generator: WorldGenerator = EmptyWorldGenerator): KarbonWorld {
        val oldWorld = lock.withLock {
            loadedWorlds[identifier]
        }
        oldWorld?.let { return it }
        val world = KarbonWorld(engine, identifier, generator)
        lock.withLock {
            loadedWorlds[identifier] = world
        }
        check(engine.scheduler.addAsyncManager(world)) { "Unable to add world $world to scheduler" }
        // TODO: WorldLoadEvent
        return world
    }

    suspend fun unloadWorld(world: KarbonWorld, save: Boolean = true) {
        val success = lock.withLock { loadedWorlds.remove(world.identifier) != null }
        if (success) {
            if (save) {
                // TODO: world saving
                engine.info("Saving $world")
            }
            check(engine.scheduler.removeAsyncManager(world)) { "Unable to remove world $world from scheduler" }
        }
    }
}