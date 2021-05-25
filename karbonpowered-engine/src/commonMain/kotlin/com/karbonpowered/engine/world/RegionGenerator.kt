package com.karbonpowered.engine.world

import com.karbonpowered.common.Named
import com.karbonpowered.engine.util.collection.map.palette.AtomicPaletteIntStore
import com.karbonpowered.engine.util.cuboid.CuboidIntBuffer
import com.karbonpowered.math.vector.intVector3Of
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class RegionGenerator(
    val region: KarbonRegion,
) : CoroutineScope, Named {
    override val name = "Region generator - (${region.x}, ${region.y}, ${region.z})"
    override val coroutineContext: CoroutineContext =
        CoroutineName(name) + Dispatchers.Default

    suspend fun generateChunk(x: Int, y: Int, z: Int): KarbonChunk? = withContext(coroutineContext) {
        val generator = region.world.generator
        if (generator.canGenerate(x, y, z)) {
            val buffer = CuboidIntBuffer(intVector3Of(0), intVector3Of(KarbonChunk.BLOCKS.SIZE))
            // TODO: Vanilla random algorithm
            val random = Random(region.world.seed * intVector3Of(x, y, z).hashCode())
            generator.generateChunk(x, y, z, random, buffer)
            val blockStore = AtomicPaletteIntStore(KarbonChunk.BLOCKS.BITS, false, dirtySize = 10, buffer.data.copyOf())
            KarbonChunk(region, x, y, z, blockStore)
        } else null
    }

    override fun toString(): String = name
}
