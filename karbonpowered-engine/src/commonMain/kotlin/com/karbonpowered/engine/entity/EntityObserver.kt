package com.karbonpowered.engine.entity

import com.karbonpowered.engine.event.EntityStartObservingChunksEvent
import com.karbonpowered.engine.event.EntityStopObservingChunksEvent
import com.karbonpowered.engine.util.AbstractObserver
import com.karbonpowered.engine.util.ChunkIterator
import com.karbonpowered.engine.world.LoadOption
import com.karbonpowered.engine.world.discrete.Transform
import com.karbonpowered.engine.world.reference.ChunkReference
import kotlinx.atomicfu.atomic
import kotlin.time.ExperimentalTime

class EntityObserver(
    val entity: KarbonEntity
) : AbstractObserver(entity.engine) {
    private var _isObserver by atomic(false)

    // TODO: Load from settings
    override var syncDistance by atomic(8)
    override var observerIterator by atomic(NO_CHUNKS)
    override var isObserver: Boolean
        get() = _isObserver
        set(value) {
            setObserver(value)
        }

    override val transform: Transform
        get() = entity.physics.transform

    override fun setObserver(observer: Boolean, chunkIterator: ChunkIterator) {
        _isObserver = observer
        observerIterator = chunkIterator
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun update() {
        val snapshotChunk = entity.physics.snapshot.value.position.chunk(engine.worldManager, LoadOption.NO_LOAD)
        val liveChunk = entity.physics.transform.position.chunk(engine.worldManager, LoadOption.NO_LOAD)
        val needsUpdate = observeChunksFailed || (isObserver && liveChunk != null && snapshotChunk != liveChunk)
        if (needsUpdate) {
            try {
                updateObserver()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun startObserving(observing: Sequence<ChunkReference>) {
        engine.eventManager.callEvent(EntityStartObservingChunksEvent(entity, observing.toSet()))
    }

    override suspend fun stopObserving(observing: Sequence<ChunkReference>) {
        engine.eventManager.callEvent(EntityStopObservingChunksEvent(entity, observing.toSet()))
    }

    override fun copySnapshot() {
    }
}