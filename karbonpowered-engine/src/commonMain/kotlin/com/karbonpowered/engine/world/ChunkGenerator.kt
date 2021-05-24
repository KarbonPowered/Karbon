package com.karbonpowered.engine.world

import com.karbonpowered.engine.util.CuboidIntBuffer
import kotlin.random.Random

interface ChunkGenerator {
    suspend fun generate(x: Int, y: Int, z: Int, buffer: CuboidIntBuffer, random: Random)
}
