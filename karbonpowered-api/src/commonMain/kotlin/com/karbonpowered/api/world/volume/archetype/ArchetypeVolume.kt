package com.karbonpowered.api.world.volume.archetype

import com.karbonpowered.api.world.volume.archetype.entity.EntityArchetypeVolume
import com.karbonpowered.api.world.volume.biome.BiomeVolume
import com.karbonpowered.api.world.volume.block.BlockVolume

interface ArchetypeVolume :
        BlockVolume.Mutable<ArchetypeVolume>,
        EntityArchetypeVolume.Mutable<ArchetypeVolume>,
        BiomeVolume.Mutable<ArchetypeVolume>