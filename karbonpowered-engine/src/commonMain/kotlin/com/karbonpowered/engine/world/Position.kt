package com.karbonpowered.engine.world

import com.karbonpowered.math.vector.FloatVector3

class Position(
    world: KarbonWorld?,
    override val x: Float,
    override val y: Float,
    override val z: Float
) : FloatVector3 {
    private val _world = world
    private val hashcode by lazy {
        var result = _world.hashCode()
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result
    }
    val world: KarbonWorld get() = requireNotNull(_world)

    val blockX get() = floorX.toInt()
    val blockY get() = floorY.toInt()
    val blockZ get() = floorZ.toInt()

    val chunkX get() = blockX shr KarbonChunk.BLOCKS.BITS
    val chunkY get() = blockY shr KarbonChunk.BLOCKS.BITS
    val chunkZ get() = blockZ shr KarbonChunk.BLOCKS.BITS

    suspend fun chunk(loadOption: LoadOption = LoadOption.LOAD_GEN) = world.getChunk(chunkX, chunkY, chunkZ, loadOption)
    fun region(loadOption: LoadOption = LoadOption.LOAD_GEN) =
        world.getRegionFromChunk(chunkX, chunkY, chunkZ, loadOption)

    override fun add(x: Float, y: Float, z: Float): Position = Position(_world, this.x + x, this.y + y, this.z + z)
    override fun add(vector: FloatVector3): Position = add(vector.x, vector.y, vector.z)
    override fun plus(vector: FloatVector3): Position = add(vector)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Position

        if (_world != other._world) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int = hashcode

    override fun toString(): String = "(${_world?.name}, $x, $y, $z)"

    companion object {
        val INVALID = Position(null, 0f, 0f, 0f)
    }
}