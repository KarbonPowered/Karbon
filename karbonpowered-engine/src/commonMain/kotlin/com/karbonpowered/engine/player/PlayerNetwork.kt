package com.karbonpowered.engine.player

import com.karbonpowered.common.collection.ConcurrentLinkedQueue
import com.karbonpowered.engine.protocol.event.ChunkSendEvent
import com.karbonpowered.engine.protocol.event.UpdateEntityEvent
import com.karbonpowered.engine.scheduler.AsyncManager
import com.karbonpowered.engine.world.KarbonChunk
import com.karbonpowered.engine.world.LoadOption
import com.karbonpowered.engine.world.discrete.Transform
import com.karbonpowered.engine.world.reference.ChunkReference
import com.karbonpowered.server.Session
import kotlinx.atomicfu.atomic

const val CHUNKS_PER_TICK = 20

open class PlayerNetwork(
    open val player: KarbonPlayer,
    val session: Session
) : AsyncManager {
    protected val chunkSendQueue = ConcurrentLinkedQueue<ChunkReference>()
    protected val chunkFreeQueue = ConcurrentLinkedQueue<ChunkReference>()
    protected val chunks = HashSet<ChunkReference>()
    protected var previousTransform by atomic(Transform.INVALID)

    fun addChunks(chunks: Collection<ChunkReference>) {
        chunkSendQueue.addAll(chunks)
    }

    fun removeChunks(toRemove: Collection<ChunkReference>) {
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
            val chunk = chunkReference.refresh(player.engine.worldManager, LoadOption.NO_LOAD)
            if (chunk != null && attemptSendChunk(chunk)) {
                chunksSentThisTick++
                iterator.remove()
            }
        }

        sendPositionUpdates(transform)
        previousTransform = transform
    }

    open fun freeChunk(chunk: ChunkReference) {
        chunks.remove(chunk)
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
        chunks.add(ChunkReference(chunk))
        return true
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