package com.karbonpowered.engine.util

import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.world.KarbonChunk
import com.karbonpowered.engine.world.discrete.Transform
import com.karbonpowered.engine.world.discrete.TransformProvider
import com.karbonpowered.engine.world.reference.ChunkReference
import com.karbonpowered.math.vector.IntVector3
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.coroutineScope

abstract class AbstractObserver(
    val engine: KarbonEngine
) : TransformProvider {
    /**
     * Gets the sync distance in [KarbonChunk]s of the owning [KarbonEntity].
     *
     * Sync distance is a value indicating the radius outwards from the entity
     * where network updates (such as chunk creation) will be triggered.
     *
     * @return The current sync distance
     */
    abstract var syncDistance: Int

    abstract var isObserver: Boolean

    open var observerIterator by atomic(NO_CHUNKS)

    protected var observeChunksFailed: Boolean = true
    protected var observingChunks: Collection<IntVector3> by atomic(emptyList())

    abstract fun setObserver(
        observer: Boolean,
        chunkIterator: ChunkIterator = if (observer) SyncDistanceChunkIterator() else NO_CHUNKS
    )

    abstract fun copySnapshot()
    abstract suspend fun update()

    suspend fun updateObserver() = coroutineScope {
        val transform = transform
        if (transform != Transform.INVALID) {
            val oldObserving = observingChunks

            val position = transform.position
            val world = requireNotNull(position.world.refresh(engine.worldManager))
            val chunkX = position.chunkX
            val chunkY = position.chunkY
            val chunkZ = position.chunkZ
            val observing = observerIterator.iterator(chunkX, chunkY, chunkZ).asSequence()
                .map { (x, y, z) -> IntVector3(x, y, z) }
                .toSet()

            val unloadChunks = oldObserving
                .asSequence()
                .filter { it !in observing }
                .map { (x, y, z) -> ChunkReference(KarbonChunk.basePosition(world, x, y, z)) }
                .toList()

            val loadChunks = observing
                .asSequence()
                .filter { it !in oldObserving }
                .map { (x, y, z) -> ChunkReference(KarbonChunk.basePosition(world, x, y, z)) }
                .toList()

            stopObserving(unloadChunks)
            startObserving(loadChunks)

            observingChunks = observing
        }
    }

    protected abstract suspend fun startObserving(observing: Iterable<ChunkReference>)
    protected abstract suspend fun stopObserving(observing: Iterable<ChunkReference>)

    inner class SyncDistanceChunkIterator : ChunkIterator {
        override fun iterator(centerX: Int, centerY: Int, centerZ: Int): Iterator<IntVector3> =
            OutwardIterator(centerX, centerY, centerZ, syncDistance)
    }

    companion object {
        val NO_CHUNKS = ChunkIterator { _, _, _ -> emptySet<IntVector3>().iterator() }
    }
}