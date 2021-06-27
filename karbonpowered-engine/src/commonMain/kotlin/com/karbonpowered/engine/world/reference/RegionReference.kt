package com.karbonpowered.engine.world.reference

import com.karbonpowered.common.reference.WeakReference
import com.karbonpowered.engine.world.KarbonRegion
import com.karbonpowered.engine.world.KarbonWorldManager
import com.karbonpowered.engine.world.LoadOption
import com.karbonpowered.engine.world.discrete.Position

class RegionReference(
    val base: Position,
    private var reference: WeakReference<KarbonRegion>? = null
) {
    constructor(region: KarbonRegion) : this(region.base)

    fun get(): KarbonRegion? =
        reference?.get()?.takeIf { it.isLoaded }

    fun refresh(manager: KarbonWorldManager, loadOption: LoadOption): KarbonRegion? {
        get()?.let { return it }
        val region = base.region(manager, loadOption)
        reference = region?.let { WeakReference(it) }
        return region
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as RegionReference

        if (base != other.base) return false

        return true
    }

    override fun hashCode(): Int = base.hashCode()

}