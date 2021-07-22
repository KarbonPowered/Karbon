package com.karbonpowered.engine.player

import com.karbonpowered.common.UUID
import com.karbonpowered.common.collection.ConcurrentLinkedQueue
import com.karbonpowered.common.collection.concurrent.synchronized
import com.karbonpowered.engine.KarbonServerEngine
import com.karbonpowered.engine.entity.KarbonEntity
import com.karbonpowered.engine.protocol.ProtocolEventListener
import com.karbonpowered.engine.protocol.event.ChunkSendEvent
import com.karbonpowered.engine.protocol.event.UpdateEntityEvent
import com.karbonpowered.engine.scheduler.AsyncManager
import com.karbonpowered.engine.world.KarbonChunk
import com.karbonpowered.engine.world.LoadOption
import com.karbonpowered.engine.world.discrete.Transform
import com.karbonpowered.engine.world.reference.ChunkReference
import com.karbonpowered.server.Session
import kotlinx.atomicfu.atomic

const val CHUNKS_PER_TICK = Int.MAX_VALUE

open class PlayerNetwork(
    open val player: KarbonPlayer,
    val session: Session
) : AsyncManager, ProtocolEventListener {
    protected val chunkSendQueue = ConcurrentLinkedQueue<ChunkReference>()
    protected val chunkFreeQueue = ConcurrentLinkedQueue<ChunkReference>()
    protected val activeChunks = HashSet<ChunkReference>()
    protected var previousTransform by atomic(Transform.INVALID)
    protected val synchronizedEntities = HashSet<UUID>().synchronized()

    val chunks: Set<ChunkReference>
        get() = activeChunks

    fun hasSpawned(entity: KarbonEntity) = synchronizedEntities.contains(entity.uniqueId)

    override fun onUpdateEntity(event: UpdateEntityEvent) {
        if (player.engine !is KarbonServerEngine) {
            return
        }
        if (event.action == UpdateEntityEvent.UpdateAction.ADD) {
            synchronizedEntities.add(event.uniqueId)
        }
        if (event.action == UpdateEntityEvent.UpdateAction.REMOVE) {
            synchronizedEntities.remove(event.uniqueId)
        }
    }

    fun addChunks(chunks: Iterable<ChunkReference>) {
        chunkSendQueue.addAll(chunks)
    }

    fun removeChunks(toRemove: Iterable<ChunkReference>) {
        chunkFreeQueue.addAll(toRemove)
    }

    override suspend fun preSnapshotRun() {
        val transform = player.transformProvider.transform

        // We want to free all chunks first
        chunkFreeQueue.forEach(::freeChunk)
        chunkFreeQueue.clear()

        sendPositionUpdates(transform)
        previousTransform = transform

        // Now send new chunks
        var chunksSentThisTick = 0

        // We always send all priority chunks
        // Send regular chunks while we aren't overloaded and we haven't exceeded our send amount
        val iterator = chunkSendQueue.iterator()
        while (iterator.hasNext() && (chunksSentThisTick < CHUNKS_PER_TICK && !player.engine.scheduler.isOverloaded)) {
            val chunkReference = iterator.next()
            val chunk = chunkReference.refresh(player.engine.worldManager, LoadOption.LOAD_GEN)
            if (chunk != null && attemptSendChunk(chunk)) {
                chunksSentThisTick++
                iterator.remove()
            }
        }

        sendPositionUpdates(transform)
        previousTransform = transform
    }

    open fun freeChunk(chunk: ChunkReference) {
        activeChunks.remove(chunk)
    }

    open suspend fun attemptSendChunk(chunk: KarbonChunk): Boolean {
        if (!canSendChunk(chunk)) {
            return false
        }
        session.callEvent(
            ChunkSendEvent(
                session,
                chunk
            )
        )
        return activeChunks.add(ChunkReference(chunk))
    }

    open fun sendPositionUpdates(transform: Transform) {
        if (previousTransform != transform) {
            session.callEvent(
                UpdateEntityEvent(
                    session,
                    player.uniqueId,
                    transform,
                    UpdateEntityEvent.UpdateAction.TRANSFORM
                )
            )
        }
    }

    protected fun canSendChunk(chunk: KarbonChunk): Boolean {
        return true
    }
}