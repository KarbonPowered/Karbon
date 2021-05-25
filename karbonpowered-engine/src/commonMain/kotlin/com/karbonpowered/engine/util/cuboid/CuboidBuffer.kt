package com.karbonpowered.engine.util.cuboid

import com.karbonpowered.engine.world.KarbonChunk
import com.karbonpowered.math.vector.IntVector3
import com.karbonpowered.math.vector.plus

abstract class CuboidBuffer(
    val base: IntVector3,
    val size: IntVector3,
) {
    val top = base + size
    val baseChunkX = base.x shr KarbonChunk.BLOCKS.BITS
    val baseChunkY = base.y shr KarbonChunk.BLOCKS.BITS
    val baseChunkZ = base.z shr KarbonChunk.BLOCKS.BITS
    val incX = 1
    val incZ = size.x * incX
    val incY = size.z * incZ
    val volume = size.x * size.y * size.z

    fun index(x: Int, y: Int, z: Int): Int {
        if (x !in base.x..top.x || y !in base.y..top.y || z !in base.z..top.z) {
            return -1
        }
        return (y - base.y) * incY + (z - base.z) * incZ + (x - base.x) * incX
    }

    fun contains(x: Int, y: Int, z: Int) = index(x, y, z) >= 0
    operator fun contains(vector: IntVector3) = contains(vector.x, vector.y, vector.z)

    override fun toString(): String = "Buffer{volume=$volume, base=$base, size=$size, top=$top}"
}
