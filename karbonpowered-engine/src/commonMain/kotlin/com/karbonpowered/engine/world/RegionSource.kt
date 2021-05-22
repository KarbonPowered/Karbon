package com.karbonpowered.engine.world

import com.karbonpowered.common.collection.concurrent.TripleIntObjectReferenceArrayMap
import com.karbonpowered.common.collection.concurrent.getOrPut
import com.karbonpowered.engine.scheduler.KarbonScheduler
import kotlinx.atomicfu.atomic

private const val REGION_MAP_BITS = 5
private const val WARN_THRESHOLD = 16

class RegionSource(
    val world: KarbonWorld
) : Iterable<KarbonRegion> {
    private val warnThreshold = atomic(WARN_THRESHOLD)
    private val regionsLoaded = atomic(0)
    private val loadedRegions = TripleIntObjectReferenceArrayMap<KarbonRegion>(REGION_MAP_BITS)

    val regions: Collection<KarbonRegion> get() = loadedRegions.values

    override fun iterator(): Iterator<KarbonRegion> = regions.iterator()

    fun getRegion(x: Int, y: Int, z: Int): KarbonRegion = loadedRegions.getOrPut(x, y, z) {
        KarbonRegion(world, x, y, z, this).also { region ->
            KarbonScheduler.addAsyncManager(region)

            val threshold = warnThreshold.value
            if (regionsLoaded.getAndIncrement() > threshold) {
                println("Warning: number of regions exceeds $threshold when creating ($x, $y, $z)")
                RuntimeException().printStackTrace()
                warnThreshold.addAndGet(5)
            }

            // TODO: Register in parallel task managers
            // TODO: Call RegionLoadEvent
        }
    }

    fun hasRegion(x: Int, y: Int, z: Int): Boolean = loadedRegions[x, y, z] != null

    fun removeRegion(region: KarbonRegion) {
        if (region.world != world) {
            return
        }

        if (loadedRegions.remove(region.x, region.y, region.z, region)) {
            // TODO: Unregistering tasks managers

            if (regionsLoaded.decrementAndGet() < 0) {
                println("Regions loaded dropped below zero")
            }

            // TODO: Call RegionUnloadEvent
            region.unlinkNeighbours()
        } else {
            println("Tried to remove region $region but region removal failed")
        }
    }
}
