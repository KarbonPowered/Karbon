package com.karbonpowered.engine.world

import com.karbonpowered.common.UUID
import com.karbonpowered.common.uuid4

class KarbonWorld(
    val name: String,
    val uniqueId: UUID = uuid4()
) {
    val regions = RegionSource(this)

    fun getRegion(x: Int, y: Int, z: Int, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        regions.getRegion(x, y, z, loadOption)

    fun getRegionFromChunk(x: Int, y: Int, z: Int, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        getRegion(
            x shr AbstractRegion.CHUNKS.BITS,
            y shr AbstractRegion.CHUNKS.BITS,
            z shr AbstractRegion.CHUNKS.BITS,
            loadOption
        )

    fun getRegionFromBlock(x: Int, y: Int, z: Int, loadOption: LoadOption = LoadOption.LOAD_GEN) =
        getRegion(
            x shr AbstractRegion.BLOCKS.BITS,
            y shr AbstractRegion.BLOCKS.BITS,
            z shr AbstractRegion.BLOCKS.BITS,
            loadOption
        )

    fun getChunk(x: Int, y: Int, z: Int, loadOption: LoadOption): KarbonChunk? {
        val region = getRegionFromChunk(x, y, z, loadOption)
        if (region != null) {
            return region.getChunk(x, y, z, loadOption)
        } else if (loadOption.isLoad && loadOption.isGenerate) {
            println("Unable to load region: $x, $y, $z: $loadOption")
        }
        return null
    }
}
