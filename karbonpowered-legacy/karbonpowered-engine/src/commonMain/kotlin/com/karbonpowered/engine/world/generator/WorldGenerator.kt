package com.karbonpowered.engine.world.generator

import com.karbonpowered.engine.util.cuboid.CuboidIntBuffer
import com.karbonpowered.engine.world.KarbonWorld

interface WorldGenerator {
    suspend fun generate(cuboidIntBuffer: CuboidIntBuffer, world: KarbonWorld)
}
