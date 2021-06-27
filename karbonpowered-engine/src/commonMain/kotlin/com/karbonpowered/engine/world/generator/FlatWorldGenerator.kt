package com.karbonpowered.engine.world.generator

import com.karbonpowered.engine.util.cuboid.CuboidIntBuffer
import com.karbonpowered.engine.world.KarbonWorld
import kotlin.math.max
import kotlin.math.min

class FlatWorldGenerator(
    val material: Int = 1
) : WorldGenerator {
    override suspend fun generateChunk(cuboidIntBuffer: CuboidIntBuffer, world: KarbonWorld) {
        val minBlockY = cuboidIntBuffer.baseY
        val maxBlockY = cuboidIntBuffer.topY
        val bottom = max(-32, minBlockY)
        val top = min(-1, maxBlockY)
        cuboidIntBuffer.fillHorizontalLayer(bottom, top - bottom, material)
    }
}