package com.karbonpowered.engine.world

import com.karbonpowered.math.vector.FloatVector3

class Position(
    val world: KarbonWorld,
    override val array: FloatArray
) : FloatVector3 {
    private val hashCode = 31 * world.hashCode() + array.contentHashCode()
    val blockX get() = floorX.toInt()
    val blockY get() = floorY.toInt()
    val blockZ get() = floorZ.toInt()

    val chunkX get() = blockX shr KarbonChunk.BLOCKS.BITS
    val chunkY get() = blockY shr KarbonChunk.BLOCKS.BITS
    val chunkZ get() = blockZ shr KarbonChunk.BLOCKS.BITS

    constructor(world: KarbonWorld, x: Float, y: Float, z: Float) : this(world, floatArrayOf(x, y, z))
    constructor(world: KarbonWorld, x: Int, y: Int, z: Int) : this(
        world,
        floatArrayOf(x.toFloat(), y.toFloat(), z.toFloat())
    )

    override fun plus(other: FloatVector3): Position = Position(
        world, x + other.x, y + other.y, z + other.z
    )

    override fun toString(): String = "($world, $x, $y, $z)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Position

        if (world != other.world) return false
        if (!array.contentEquals(other.array)) return false

        return true
    }

    override fun hashCode(): Int = hashCode
}