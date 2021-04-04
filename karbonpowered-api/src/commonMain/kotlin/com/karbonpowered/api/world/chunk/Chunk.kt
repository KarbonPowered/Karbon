package com.karbonpowered.api.world.chunk

import com.karbonpowered.api.world.volume.MutableBlockVolume
import com.karbonpowered.math.vector.IntVector3

interface Chunk<P : Chunk<P>> : MutableBlockVolume<P> {
    val isEmpty: Boolean
    val chunkPosition: IntVector3

    val isLoaded: Boolean
}