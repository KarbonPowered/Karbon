package com.karbonpowered.engine.world

import com.karbonpowered.common.UUID
import com.karbonpowered.common.uuid4
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.component.BaseComponentHolder
import com.karbonpowered.engine.component.EntityComponent
import com.karbonpowered.engine.component.WorldComponent
import com.karbonpowered.engine.entity.KarbonEntity
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.engine.scheduler.AsyncManager
import com.karbonpowered.engine.util.Transform
import com.karbonpowered.engine.world.generator.WorldGenerator
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlin.random.Random

open class KarbonWorld(
    val engine: KarbonEngine,
    val name: String,
    val generator: WorldGenerator,
    val uniqueId: UUID = uuid4(),
    val seed: Long = Random.nextLong()
) : AsyncManager {
    private val lock = reentrantLock()

    val regions = RegionSource(this)
    val components = BaseComponentHolder(engine)

    val _players = ArrayList<KarbonPlayer>()
    val players get() = _players.toList()

    private val _spawnPoint = Transform(Position(this, 0f, 64f, 0f))
    var spawnPoint: Transform
        get() = _spawnPoint.copy()
        set(value) {
            _spawnPoint.set(value)
        }

    fun getRegion(x: Int, y: Int, z: Int, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        regions.getRegion(x, y, z, loadOption)

    fun getRegionFromChunk(x: Int, y: Int, z: Int, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        getRegion(
            x shr KarbonRegion.CHUNKS.BITS,
            y shr KarbonRegion.CHUNKS.BITS,
            z shr KarbonRegion.CHUNKS.BITS,
            loadOption
        )

    fun getRegionFromBlock(x: Int, y: Int, z: Int, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        getRegion(
            x shr KarbonRegion.BLOCKS.BITS,
            y shr KarbonRegion.BLOCKS.BITS,
            z shr KarbonRegion.BLOCKS.BITS,
            loadOption
        )

    suspend fun getChunk(x: Int, y: Int, z: Int, loadOption: LoadOption): KarbonChunk? {
        val region = getRegionFromChunk(x, y, z, loadOption)
        if (region != null) {
            return region.getChunk(x, y, z, loadOption)
        } else if (loadOption.isLoad && loadOption.isGenerate) {
            println("Unable to load region: $x, $y, $z: $loadOption")
        }
        return null
    }

    fun spawnEntity(entity: KarbonEntity) {
        val region = checkNotNull(entity.region)
        // TODO: Entity spawn event
        region.entityManager.addEntity(entity)
        components.forEach { component ->
            if (component is WorldComponent) {
                component.onSpawn(entity)
            }
        }
        entity.components.forEach { component ->
            if (component is EntityComponent) {
                component.onSpawned()
            }
        }
    }

    fun addPlayer(player: KarbonPlayer) {
        lock.withLock {
            _players.add(player)
        }
    }

    fun removePlayer(player: KarbonPlayer) {
        lock.withLock {
            _players.remove(player)
        }
    }
}
