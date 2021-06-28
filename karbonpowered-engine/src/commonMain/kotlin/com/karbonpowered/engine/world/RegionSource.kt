package com.karbonpowered.engine.world

import com.karbonpowered.common.collection.concurrent.TripleIntObjectReferenceArrayMap

private const val REGION_MAP_BITS = 5

class RegionSource(
    val world: KarbonWorld
) : Iterable<KarbonRegion> {
    private val loadedRegions = TripleIntObjectReferenceArrayMap<KarbonRegion>(REGION_MAP_BITS)

    val regions: Collection<KarbonRegion> get() = loadedRegions.values

    override fun iterator(): Iterator<KarbonRegion> = regions.iterator()

    fun getRegion(x: Int, y: Int, z: Int, loadOption: LoadOption): KarbonRegion = loadedRegions.getOrPut(x, y, z) {
        KarbonRegion(world, x, y, z, this).also { region ->
            world.engine.scheduler.addAsyncManager(region)
        }
    }

    fun hasRegion(x: Int, y: Int, z: Int): Boolean = loadedRegions[x, y, z] != null
}
