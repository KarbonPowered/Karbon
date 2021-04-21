package com.karbonpowered.api.world

import com.karbonpowered.api.context.ContextSource
import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.api.world.biome.BiomeSampler

interface WorldType : DefaultedRegistryValue, ContextSource {
    val effect: WorldTypeEffect
    val biomeSampler: BiomeSampler
    val scorching: Boolean
    val natural: Boolean
    val coordinateMultiplier: Double
    val hasSkylight: Boolean
    val hasCeiling: Boolean
    val ambientLighting: Float
    val fixedTime: Long?
    val piglinSafe: Boolean
    val bedsUsable: Boolean
    val respawnAnchorsUsable: Boolean
    val hasRaids: Boolean
    val logicalHeight: Int
    val createDragonFight: Boolean
    fun asTemplate(): WorldTypeTemplate
}