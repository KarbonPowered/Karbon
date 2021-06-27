package com.karbonpowered.engine.world.generator

import com.karbonpowered.engine.util.cuboid.CuboidIntBuffer
import com.karbonpowered.engine.world.KarbonWorld

interface WorldGenerator {
    suspend fun generateChunk(cuboidIntBuffer: CuboidIntBuffer, world: KarbonWorld)
}
