package com.karbonpowered.engine.world

import com.karbonpowered.engine.scheduler.AsyncManager
import kotlinx.atomicfu.AtomicRef

class KarbonRegion(
    val world: KarbonWorld,
    val x: Int,
    val y: Int,
    val z: Int,
    val regionSource: RegionSource
) : AbstractRegion(), AsyncManager {
    private val chunks: Array<Array<Array<AtomicRef<KarbonChunk>?>>> =
        Array(CHUNKS.SIZE) { Array(CHUNKS.SIZE) { Array(CHUNKS.SIZE) { null } } }

    fun unlinkNeighbours() {
        TODO()
    }

    override fun getChunk(x: Int, y: Int, z: Int, loadOption: LoadOption): KarbonChunk {
        TODO("Not yet implemented")
    }
}
