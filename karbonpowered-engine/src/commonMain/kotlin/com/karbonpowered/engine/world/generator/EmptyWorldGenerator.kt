package com.karbonpowered.engine.world.generator

import com.karbonpowered.engine.util.cuboid.CuboidIntBuffer
import com.karbonpowered.engine.world.KarbonWorld

object EmptyWorldGenerator : WorldGenerator {
    override suspend fun generateChunk(cuboidIntBuffer: CuboidIntBuffer, world: KarbonWorld) {
    }
}