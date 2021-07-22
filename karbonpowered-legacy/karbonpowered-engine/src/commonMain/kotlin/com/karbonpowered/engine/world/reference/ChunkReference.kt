package com.karbonpowered.engine.world.reference

import com.karbonpowered.common.reference.WeakReference
import com.karbonpowered.engine.world.KarbonChunk
import com.karbonpowered.engine.world.KarbonRegion
import com.karbonpowered.engine.world.KarbonWorldManager
import com.karbonpowered.engine.world.LoadOption
import com.karbonpowered.engine.world.discrete.Position

class ChunkReference(
    val base: Position,
    val region: RegionReference = RegionReference(
        Position(
            base.world,
            base.blockX and KarbonRegion.BLOCKS.MASK,
            base.blockY and KarbonRegion.BLOCKS.MASK,
            base.blockZ and KarbonRegion.BLOCKS.MASK
        )
    ),
    private var reference: WeakReference<KarbonChunk>? = null
) {
    constructor(chunk: KarbonChunk) : this(chunk.base)

    fun get(): KarbonChunk? =
        reference?.get()?.takeIf { it.isLoaded }

    suspend fun refresh(manager: KarbonWorldManager, loadOption: LoadOption = LoadOption.LOAD_GEN): KarbonChunk? {
        get()?.let { return it }
        val chunk = base.chunk(manager, loadOption)
        reference = chunk?.let { WeakReference(it) }
        return chunk
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ChunkReference

        if (base.chunkX != other.base.chunkX) return false
        if (base.chunkY != other.base.chunkY) return false
        if (base.chunkZ != other.base.chunkZ) return false
        if (base.world.identifier != other.base.world.identifier) return false

        return true
    }

    override fun hashCode(): Int = base.hashCode()

    override fun toString() = "ChunkReference(x=${base.chunkX}, y=${base.chunkY}, z=${base.chunkZ}, ref=${get()})"
}