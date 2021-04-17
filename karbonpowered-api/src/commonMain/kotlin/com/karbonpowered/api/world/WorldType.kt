package com.karbonpowered.api.world

import com.karbonpowered.api.Karbon
import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.context.ContextSource
import com.karbonpowered.api.registry.DefaultedRegistryReference
import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.api.registry.RegistryKey
import com.karbonpowered.api.registry.RegistryTypes
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

object WorldTypes {
    val OVERWORLD by key(ResourceKey.minecraft("overworld"))
    val OVERWORLD_CAVES by key(ResourceKey.minecraft("overworld_caves"))
    val THE_NETHER by key(ResourceKey.minecraft("the_nether"))
    val THE_END by key(ResourceKey.minecraft("the_end"))

    private fun key(location: ResourceKey): DefaultedRegistryReference<WorldType> =
            RegistryKey(RegistryTypes.WORLD_TYPE, location).asDefaultedReference { Karbon.game.registries }
}