package com.karbonpowered.engine.world.generator

import com.karbonpowered.engine.util.cuboid.CuboidIntBuffer
import kotlin.random.Random

object EmptyWorldGenerator : WorldGenerator {
    override suspend fun generateChunk(x: Int, y: Int, z: Int, random: Random, buffer: CuboidIntBuffer) {
    }
}