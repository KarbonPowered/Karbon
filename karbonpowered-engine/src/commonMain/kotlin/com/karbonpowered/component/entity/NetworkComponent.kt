package com.karbonpowered.component.entity

import com.karbonpowered.api.world.WorldLoadOption
import com.karbonpowered.engine.util.OutwardIterator
import com.karbonpowered.engine.world.KarbonChunk
import com.karbonpowered.engine.world.KarbonWorld
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.math.vector.IntVector3
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

open class NetworkComponent : EntityComponent() {
    override fun canTick(): Boolean = false
    val syncDistance = 10
    val observingChunks = HashSet<KarbonChunk>()

    var observer: Boolean = false
    var observeChunksFailed = true

    fun finalizeRun(position: DoubleVector3, rotation: DoubleVector3) {
        if (observeChunksFailed) {
            updateObservers()
        }
    }

    protected fun updateObservers() {
        val location = owner.location
        val world = requireNotNull(location.world) as KarbonWorld
        val cx = location.chunkPosition.x
        val cy = location.chunkPosition.y
        val cz = location.chunkPosition.z

        val observing = HashSet<KarbonChunk>((syncDistance * syncDistance * syncDistance * 3) / 2)
        val ungeneratedChunks = ArrayList<IntVector3>()
        val iterator = OutwardIterator(cx, cy, cz, syncDistance)
        observeChunksFailed = false
        while (iterator.hasNext()) {
            val chunkPos = iterator.next()
            val chunk = world.chunk(chunkPos.x, chunkPos.y, chunkPos.z, WorldLoadOption.LOAD_ONLY) as? KarbonChunk
            if (chunk != null) {
                chunk.refreshObserver(owner)
                observing.add(chunk)
            } else {
                ungeneratedChunks.add(chunkPos)
                observeChunksFailed = true
            }
        }
        observingChunks.removeAll(observing)
        observingChunks.forEach { chunk ->
//            if (chunk.isLoaded) {
//                (chunk as KarbonChunk).removeObserver(owner)
//            }
        }
        observingChunks.clear()
        observingChunks.addAll(observing)
        if (!ungeneratedChunks.isEmpty()) {
//            world.queueChunksForGeneration(ungeneratedChunks)
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun tick(duration: Duration) {
        if (canTick()) {
            onTick(duration)
        }
    }
}