package com.karbonpowered.engine.world.discrete

import com.karbonpowered.data.ResourceKey
import com.karbonpowered.engine.world.KarbonChunk
import com.karbonpowered.engine.world.KarbonWorld
import com.karbonpowered.engine.world.KarbonWorldManager
import com.karbonpowered.engine.world.LoadOption
import com.karbonpowered.engine.world.reference.WorldReference
import com.karbonpowered.math.vector.FloatVector3
import com.karbonpowered.math.vector.IntVector3
import kotlinx.serialization.Serializable

@Serializable
class Position(
    val world: WorldReference = WorldReference.EMPTY,
    override val x: Float = 0f,
    override val y: Float = 0f,
    override val z: Float = 0f
) : FloatVector3 {

    private val hashcode by lazy {
        var result = world.hashCode()
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result
    }

    constructor(world: WorldReference, x: Int, y: Int, z: Int) : this(world, x.toFloat(), y.toFloat(), z.toFloat())
    constructor(world: WorldReference, vector: FloatVector3) : this(world, vector.x, vector.y, vector.z)
    constructor(world: WorldReference, vector: IntVector3) : this(
        world,
        vector.x.toFloat(),
        vector.y.toFloat(),
        vector.z.toFloat()
    )

    constructor(world: KarbonWorld, x: Int, y: Int, z: Int) : this(WorldReference(world), x, y, z)
    constructor(world: KarbonWorld, x: Float, y: Float, z: Float) : this(WorldReference(world), x, y, z)
    constructor(world: KarbonWorld, vector: FloatVector3) : this(WorldReference(world), vector.x, vector.y, vector.z)
    constructor(world: KarbonWorld, vector: IntVector3) : this(
        WorldReference(world),
        vector.x.toFloat(),
        vector.y.toFloat(),
        vector.z.toFloat()
    )

    constructor(world: ResourceKey, x: Int, y: Int, z: Int) : this(WorldReference(world), x, y, z)
    constructor(world: ResourceKey, x: Float, y: Float, z: Float) : this(WorldReference(world), x, y, z)
    constructor(world: ResourceKey, vector: FloatVector3) : this(WorldReference(world), vector.x, vector.y, vector.z)
    constructor(world: ResourceKey, vector: IntVector3) : this(
        WorldReference(world),
        vector.x.toFloat(),
        vector.y.toFloat(),
        vector.z.toFloat()
    )

    val blockX get() = floorX.toInt()
    val blockY get() = floorY.toInt()
    val blockZ get() = floorZ.toInt()

    val chunkX get() = blockX shr KarbonChunk.BLOCKS.BITS
    val chunkY get() = blockY shr KarbonChunk.BLOCKS.BITS
    val chunkZ get() = blockZ shr KarbonChunk.BLOCKS.BITS

    suspend fun chunk(worldManager: KarbonWorldManager, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        world.refresh(worldManager)?.getChunk(chunkX, chunkY, chunkZ, loadOption)

    fun region(worldManager: KarbonWorldManager, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        world.refresh(worldManager)?.getRegionFromChunk(chunkX, chunkY, chunkZ, loadOption)

    override fun add(x: Float, y: Float, z: Float): Position = Position(world, this.x + x, this.y + y, this.z + z)
//    override fun sub(x: Float, y: Float, z: Float): Position = Position(world, this.x - x, this.y - y, this.z - z)
//    override fun mul(x: Float, y: Float, z: Float): Position = Position(world, this.x * x, this.y * y, this.z * z)
//    override fun div(x: Float, y: Float, z: Float): Position = Position(world, this.x / x, this.y / y, this.z / z)

    override fun plus(vector: FloatVector3): Position = Position(world, this.x + x, this.y + y, this.z + z)
//    override fun minus(vector: FloatVector3): Position = Position(world, this.x - x, this.y - y, this.z - z)
//    override fun times(vector: FloatVector3): Position = Position(world, this.x * x, this.y * y, this.z * z)
//    override fun div(vector: FloatVector3): Position = Position(world, this.x / x, this.y / y, this.z / z)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Position

        if (world != other.world) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int = hashcode

    override fun toString(): String = "(${world.identifier}, $x, $y, $z)"

    companion object {
        val INVALID = Position()
    }
}
