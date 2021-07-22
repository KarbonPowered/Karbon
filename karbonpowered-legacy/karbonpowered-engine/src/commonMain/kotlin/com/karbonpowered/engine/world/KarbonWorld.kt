package com.karbonpowered.engine.world

import com.karbonpowered.common.UUID
import com.karbonpowered.common.uuid4
import com.karbonpowered.data.ResourceKey
import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.component.BaseComponentHolder
import com.karbonpowered.engine.entity.KarbonEntity
import com.karbonpowered.engine.player.KarbonPlayer
import com.karbonpowered.engine.scheduler.AsyncManager
import com.karbonpowered.engine.world.discrete.Position
import com.karbonpowered.engine.world.discrete.Transform
import com.karbonpowered.engine.world.generator.WorldGenerator
import com.karbonpowered.math.imaginary.FloatQuaternion
import com.karbonpowered.math.vector.FloatVector3
import kotlinx.atomicfu.locks.reentrantLock
import kotlin.random.Random

open class KarbonWorld(
    val engine: KarbonEngine,
    val identifier: ResourceKey,
    val generator: WorldGenerator,
    val uniqueId: UUID = uuid4(),
    val seed: Long = Random.nextLong()
) : AsyncManager {
    private val lock = reentrantLock()

    val regions = RegionSource(this)
    val components = BaseComponentHolder(engine)

    val _players = ArrayList<KarbonPlayer>()
    val players get() = _players.toList()
    val isLoaded: Boolean get() = true

    var spawnPoint = Transform(Position(this, 0f, 64f, 0f))

    fun getRegion(x: Int, y: Int, z: Int, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        regions.getRegion(x, y, z, loadOption)

    fun getRegionFromChunk(x: Int, y: Int, z: Int, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        getRegion(
            x shr KarbonRegion.CHUNKS.BITS,
            y shr KarbonRegion.CHUNKS.BITS,
            z shr KarbonRegion.CHUNKS.BITS,
            loadOption
        )

    fun getRegionFromBlock(vector: FloatVector3, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        getRegionFromBlock(vector.floorX.toInt(), vector.floorX.toInt(), vector.floorX.toInt(), loadOption)

    fun getRegionFromBlock(x: Int, y: Int, z: Int, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        getRegion(
            x shr KarbonRegion.BLOCKS.BITS,
            y shr KarbonRegion.BLOCKS.BITS,
            z shr KarbonRegion.BLOCKS.BITS,
            loadOption
        )

    suspend fun getChunk(x: Int, y: Int, z: Int, loadOption: LoadOption): KarbonChunk? {
        val region = getRegionFromChunk(x, y, z, loadOption)
        return region.chunk(x, y, z, loadOption)
    }

    fun spawnEntity(
        position: FloatVector3,
        uniqueId: UUID = uuid4(),
        option: LoadOption = LoadOption.LOAD_GEN
    ): KarbonEntity {
        val region = requireNotNull(getRegionFromBlock(position, option))
        val entity = KarbonEntity(
            engine,
            Transform(Position(this, position), FloatQuaternion.fromAxesAnglesDeg(0f, 0f, 0f)),
            uniqueId
        )
        region.entityManager.addEntity(entity)
        return entity
    }

    override fun toString(): String = "World($identifier)"
}
