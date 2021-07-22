package com.karbonpowered.api.world.chunk

import com.karbonpowered.api.world.World
import com.karbonpowered.api.world.volume.entity.EntityVolume

interface Chunk : ProtoChunk<Chunk>, EntityVolume.Mutable<Chunk> {
    override val world: World<*, *>
}