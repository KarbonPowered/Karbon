package com.karbonpowered.engine.util

import com.karbonpowered.engine.KarbonEngine
import com.karbonpowered.engine.world.discrete.Transform
import com.karbonpowered.engine.world.discrete.TransformProvider
import com.karbonpowered.math.vector.IntVector3
import kotlinx.atomicfu.atomic

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

    protected var observeChunksFailed: Boolean = false

    abstract fun setObserver(
        observer: Boolean,
        chunkIterator: ChunkIterator = if (observer) SyncDistanceChunkIterator() else NO_CHUNKS
    )

    abstract fun copySnapshot()
    abstract suspend fun update()

    suspend fun updateObserver() {
        val transform = transform
        if (transform != Transform.INVALID) {

        }
    }

    inner class SyncDistanceChunkIterator : ChunkIterator {
        override fun iterator(centerX: Int, centerY: Int, centerZ: Int): Iterator<IntVector3> =
            OutwardIterator(centerX, centerY, centerZ, syncDistance)
    }

    companion object {
        val NO_CHUNKS = ChunkIterator { _, _, _ -> emptySet<IntVector3>().iterator() }
    }
}