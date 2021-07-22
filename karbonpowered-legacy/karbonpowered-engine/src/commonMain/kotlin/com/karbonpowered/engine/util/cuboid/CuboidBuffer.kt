package com.karbonpowered.engine.util.cuboid

import com.karbonpowered.engine.world.KarbonChunk
import com.karbonpowered.math.vector.FloatVector3
import com.karbonpowered.math.vector.IntVector3
import kotlin.math.max
import kotlin.math.min

abstract class CuboidBuffer(
    val baseX: Int,
    val baseY: Int,
    val baseZ: Int,
    val sizeX: Int,
    val sizeY: Int,
    val sizeZ: Int
) {
    val topX = baseX + sizeX
    val topY = baseY + sizeY
    val topZ = baseZ + sizeZ
    val baseChunkX = baseX shr KarbonChunk.BLOCKS.BITS
    val baseChunkY = baseY shr KarbonChunk.BLOCKS.BITS
    val baseChunkZ = baseZ shr KarbonChunk.BLOCKS.BITS
    val incX = 1
    val incZ = sizeX * incX
    val incY = sizeZ * incZ
    val volume = sizeX * sizeY * sizeZ
    val Xinc = 1
    val Zinc = sizeX * Xinc
    val Yinc = sizeZ * Zinc

    val base get() = FloatVector3(baseX.toFloat(), baseY.toFloat(), baseZ.toFloat())
    val size get() = FloatVector3(sizeZ.toFloat(), sizeY.toFloat(), sizeZ.toFloat())
    val top get() = base + size

    constructor(base: IntVector3, size: IntVector3) : this(base.x, base.y, base.z, size.x, size.y, size.z)

    fun index(x: Int, y: Int, z: Int): Int {
        if (x !in baseX..topX || y !in baseY..topY || z !in baseZ..topZ) {
            return -1
        }
        return (y - baseY) * incY + (z - baseZ) * incZ + (x - baseX) * incX
    }

    fun copyFrom(source: CuboidBuffer) {
        val overlapBaseX = max(source.baseX, baseX)
        val overlapBaseY = max(source.baseY, baseY)
        val overlapBaseZ = max(source.baseZ, baseZ)

        val overlapSizeX = min(source.topX, topX) - overlapBaseX
        val overlapSizeY = min(source.topY, topY) - overlapBaseY
        val overlapSizeZ = min(source.topZ, topZ) - overlapBaseZ

        var sourceIndex: Int
        var targetIndex: Int

        if (overlapSizeX < 0 || overlapSizeY < 0 || overlapSizeZ < 0) {
            sourceIndex = -1
            targetIndex = -1
        } else {
            sourceIndex = source.index(overlapBaseX, overlapBaseY, overlapBaseZ)
            targetIndex = index(overlapBaseX, overlapBaseY, overlapBaseZ)
        }

        if (!(sourceIndex == -1 || targetIndex == -1)) {
            for (x in 0 until overlapSizeY) {
                var outerSourceIndex = sourceIndex
                var outerThisIndex = targetIndex
                for (z in 0 until overlapSizeZ) {
                    copyElement(outerThisIndex, outerSourceIndex, overlapSizeX, source)

                    outerSourceIndex += source.Zinc
                    outerThisIndex += Zinc
                }
                sourceIndex += source.Yinc
                targetIndex += Yinc
            }
        }
    }

    abstract fun copyElement(thisIndex: Int, sourceIndex: Int, runLength: Int, source: CuboidBuffer)

    fun contains(x: Int, y: Int, z: Int) = index(x, y, z) >= 0
    operator fun contains(vector: IntVector3) = contains(vector.x, vector.y, vector.z)

    override fun toString(): String = "Buffer{volume=$volume, base=$base, size=$size, top=$top}"
}
