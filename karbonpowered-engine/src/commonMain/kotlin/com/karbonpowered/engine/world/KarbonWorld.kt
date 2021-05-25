package com.karbonpowered.engine.world

import com.karbonpowered.common.UUID
import com.karbonpowered.common.uuid4
import kotlin.random.Random

class KarbonWorld(
    val name: String,
    val generator: WorldGenerator,
    val uniqueId: UUID = uuid4(),
    val seed: Long = Random.nextLong()
) {
    val regions = RegionSource(this)

    fun getRegion(x: Int, y: Int, z: Int, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        regions.getRegion(x, y, z, loadOption)

    fun getRegionFromChunk(x: Int, y: Int, z: Int, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        getRegion(
            x shr KarbonRegion.CHUNKS.BITS,
            y shr KarbonRegion.CHUNKS.BITS,
            z shr KarbonRegion.CHUNKS.BITS,
            loadOption
        )

    fun getRegionFromBlock(x: Int, y: Int, z: Int, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        getRegion(
            x shr KarbonRegion.BLOCKS.BITS,
            y shr KarbonRegion.BLOCKS.BITS,
            z shr KarbonRegion.BLOCKS.BITS,
            loadOption
        )

    suspend fun getChunk(x: Int, y: Int, z: Int, loadOption: LoadOption): KarbonChunk? {
        val region = getRegionFromChunk(x, y, z, loadOption)
        if (region != null) {
            return region.getChunk(x, y, z, loadOption)
        } else if (loadOption.isLoad && loadOption.isGenerate) {
            println("Unable to load region: $x, $y, $z: $loadOption")
        }
        return null
    }
}
