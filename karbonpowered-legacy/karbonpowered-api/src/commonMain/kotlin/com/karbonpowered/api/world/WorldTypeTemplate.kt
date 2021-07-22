package com.karbonpowered.api.world

import com.karbonpowered.api.world.biome.BiomeSampler

interface WorldTypeTemplate {
    val effect: WorldTypeEffect
    val biomeSampler: BiomeSampler
    val scorching: Boolean
    val natural: Boolean
    val coordinateMultiplier: Double
    val hasSkylight: Boolean
    val hasCelling: Boolean
    val ambientLighting: Float
    val fixedTime: Long
    val piglinSafe: Boolean
    val bedsUsable: Boolean
    val respawnAnchorsUsable: Boolean
    val hasRaids: Boolean
    val logicalHeight: Int
    val createDragonFight: Boolean

    companion object
}