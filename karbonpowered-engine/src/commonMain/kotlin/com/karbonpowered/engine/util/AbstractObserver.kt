package com.karbonpowered.engine.util

import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.world.KarbonChunk
import com.karbonpowered.engine.world.LoadOption
import com.karbonpowered.engine.world.discrete.Transform
import com.karbonpowered.engine.world.discrete.TransformProvider
import com.karbonpowered.engine.world.reference.ChunkReference
import com.karbonpowered.engine.world.reference.WorldReference
import com.karbonpowered.math.vector.IntVector3
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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
    protected var observingChunks by atomic(setOf<ChunkReference>())

    abstract fun setObserver(
        observer: Boolean,
        chunkIterator: ChunkIterator = if (observer) SyncDistanceChunkIterator() else NO_CHUNKS
    )

    abstract fun copySnapshot()
    abstract suspend fun update()

    suspend fun updateObserver() = coroutineScope {
        val transform = transform
        val oldObserving = observingChunks.toMutableSet()
        val newObserving = mutableSetOf<ChunkReference>()
        val observing = mutableSetOf<ChunkReference>()
        if (transform != Transform.INVALID) {
            launch {
                val position = transform.position
                val world = requireNotNull(position.world.refresh(engine.worldManager))
                val chunkX = position.chunkX
                val chunkY = position.chunkY
                val chunkZ = position.chunkZ
                val iterator = observerIterator.iterator(chunkX, chunkY, chunkZ)
                iterator.forEach { (x, y, z) ->
                    launch { world.getChunk(x, y, z, LoadOption.LOAD_GEN) }
                    val chunk = ChunkReference(KarbonChunk.basePosition(WorldReference(world), x, y, z))
                    observing.add(chunk)
                    if (!oldObserving.contains(chunk)) {
                        newObserving.add(chunk)
                    }
                }
                oldObserving.removeAll(observing)
            }.join()
        }
        if (oldObserving.isNotEmpty()) {
            stopObserving(oldObserving.asSequence())
        }
        if (newObserving.isNotEmpty()) {
            startObserving(newObserving.asSequence())
        }
        observingChunks = observing
    }

    protected abstract suspend fun startObserving(observing: Sequence<ChunkReference>)
    protected abstract suspend fun stopObserving(observing: Sequence<ChunkReference>)

    inner class SyncDistanceChunkIterator : ChunkIterator {
        override fun iterator(centerX: Int, centerY: Int, centerZ: Int): Iterator<IntVector3> =
            OutwardIterator(centerX, centerY, centerZ, syncDistance)
    }

    companion object {
        val NO_CHUNKS = ChunkIterator { _, _, _ -> emptySet<IntVector3>().iterator() }
    }
}