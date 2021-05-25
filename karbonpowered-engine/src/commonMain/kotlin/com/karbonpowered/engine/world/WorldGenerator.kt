package com.karbonpowered.engine.world

import com.karbonpowered.engine.util.cuboid.CuboidIntBuffer
import kotlin.random.Random

interface WorldGenerator {
    fun canGenerate(x: Int, y: Int, z: Int): Boolean = true

    suspend fun generateChunk(x: Int, y: Int, z: Int, random: Random, buffer: CuboidIntBuffer)
}
