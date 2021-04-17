package com.karbonpowered.engine.world

import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.block.entity.BlockEntity
import com.karbonpowered.api.entity.Entity
import com.karbonpowered.api.entity.EntityType
import com.karbonpowered.api.entity.living.player.Player
import com.karbonpowered.api.fluid.FluidState
import com.karbonpowered.api.world.biome.Biome
import com.karbonpowered.api.world.chunk.Chunk
import com.karbonpowered.api.world.chunk.ChunkState
import com.karbonpowered.api.world.volume.sequence.SequenceOptions
import com.karbonpowered.api.world.volume.sequence.VolumeSequence
import com.karbonpowered.common.UUID
import com.karbonpowered.engine.entity.KarbonEntity
import com.karbonpowered.engine.entity.KarbonPlayer
import com.karbonpowered.engine.util.BitSize
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.math.vector.IntVector3
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock

class KarbonChunk(
        override val world: KarbonWorld,
        val x: Int,
        val y: Int,
        val z: Int
) : Chunk {
    val observerLock = reentrantLock()
    val observers = HashSet<KarbonEntity<*>>()
    val playerObservers = HashSet<KarbonPlayer>()

    companion object {
        val BLOCKS = BitSize(4)
    }

    protected val saveState = atomic(SaveState.NONE)
    override val state: ChunkState
        get() = TODO("Not yet implemented")
    override val chunkPosition: IntVector3
        get() = TODO("Not yet implemented")
    override val regionalDifficultyFactor: Double
        get() = TODO("Not yet implemented")
    override val regionalDifficultyPercentage: Double
        get() = TODO("Not yet implemented")
    override var inhabitedTime: Double
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun addEntity(entity: Entity<*>) {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setBlock(x: Int, y: Int, z: Int, blockState: BlockState): Boolean {
        return true
    }

    override fun removeBlock(x: Int, y: Int, z: Int): Boolean {
        return true
    }

    override fun addBlockEntity(x: Int, y: Int, z: Int, blockEntity: BlockEntity) {
        TODO("Not yet implemented")
    }

    override fun removeBlockEntity(x: Int, y: Int, z: Int) {
        TODO("Not yet implemented")
    }

    override fun blockEntitySequence(min: IntVector3, max: IntVector3, options: SequenceOptions): VolumeSequence<Chunk, BlockEntity> {
        TODO("Not yet implemented")
    }

    override val blockEntities: Collection<BlockEntity>
        get() = TODO("Not yet implemented")


    override fun blockEntity(x: Int, y: Int, z: Int): BlockEntity? {
        TODO("Not yet implemented")
    }

    override fun block(x: Int, y: Int, z: Int): BlockState {
        TODO("Not yet implemented")
    }

    override fun fluid(x: Int, y: Int, z: Int): FluidState {
        TODO("Not yet implemented")
    }

    override fun highestYAt(x: Int, z: Int): Int {
        TODO("Not yet implemented")
    }

    override fun setBiome(x: Int, y: Int, z: Int, biome: Biome): Boolean {
        TODO("Not yet implemented")
    }

    override fun biomeSequence(min: IntVector3, max: IntVector3, option: SequenceOptions): VolumeSequence<Chunk, Biome> {
        TODO("Not yet implemented")
    }

    override fun biome(x: Int, y: Int, z: Int): Biome {
        TODO("Not yet implemented")
    }

    override fun <E : Entity<E>> createEntity(type: EntityType<E>, position: DoubleVector3): E {
        TODO("Not yet implemented")
    }

    override fun <E : Entity<E>> createEntityNaturally(type: EntityType<E>, position: DoubleVector3): E {
        TODO("Not yet implemented")
    }

    override fun spawnEntity(entity: Entity<*>): Boolean {
        TODO("Not yet implemented")
    }

    override fun spawnEntities(entities: Iterable<Entity<*>>): Collection<Entity<*>> {
        TODO("Not yet implemented")
    }

    override fun entitySequence(min: IntVector3, max: IntVector3, options: SequenceOptions): VolumeSequence<Chunk, Entity<*>> {
        TODO("Not yet implemented")
    }

    override val players: Collection<Player>
        get() = TODO("Not yet implemented")

    override fun entity(uuid: UUID): Entity<*>? {
        TODO("Not yet implemented")
    }

    fun cancelUnload() = SaveState.cancelUnload(saveState)

    fun refreshObserver(entity: Entity<*>): Boolean {
        observerLock.withLock {
            val wasEmpty = observers.isEmpty()
            if (observers.add(entity as KarbonEntity<*>) && entity is KarbonPlayer) {
                playerObservers.add(entity)
            }
        }
        return true
    }

    fun removeObserver(entity: Entity<*>) {
        observerLock.withLock {
            if (observers.remove(entity) && entity is KarbonPlayer) {
                playerObservers.remove(entity)
            }
            if (observers.isEmpty()) {
                // TODO: Unload chunk
            }
        }
    }


    enum class SaveState {
        UNLOAD_SAVE,
        UNLOAD,
        SAVE,
        NONE,
        SAVING,
        POST_SAVED,
        UNLOADED;

        companion object {
            fun cancelUnload(saveState: AtomicRef<SaveState>): Boolean {
                var success = false
                var oldState: SaveState? = null
                while (!success) {
                    oldState = saveState.value
                    val nextState = when (oldState) {
                        UNLOAD_SAVE -> SAVE
                        UNLOAD -> NONE
                        SAVE -> SAVE
                        NONE -> NONE
                        SAVING -> NONE
                        POST_SAVED -> NONE
                        UNLOADED -> UNLOADED
                    }
                    success = saveState.compareAndSet(oldState, nextState)
                }
                return oldState != UNLOADED
            }
        }
    }

}