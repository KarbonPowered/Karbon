package com.karbonpowered.api.world

import com.karbonpowered.api.Engine
import com.karbonpowered.api.world.volume.biome.BiomeVolume
import com.karbonpowered.api.world.volume.block.BlockVolume
import com.karbonpowered.api.world.volume.block.PhysicsAwareMutableBlockVolume
import com.karbonpowered.api.world.volume.block.entity.BlockEntityVolume
import com.karbonpowered.api.world.volume.entity.EntityVolume
import com.karbonpowered.api.world.volume.game.GenerationVolume
import com.karbonpowered.api.world.volume.game.LocationBaseDataHolder
import com.karbonpowered.api.world.volume.game.UpdatableVolume
import com.karbonpowered.common.RandomProvider

interface ProtoWorld<P : ProtoWorld<P>> :
        BiomeVolume.Mutable<P>,
        BlockVolume.Mutable<P>,
        EntityVolume.Mutable<P>,
        BlockEntityVolume.Mutable<P>,
        GenerationVolume.Mutable,
        LocationBaseDataHolder.Mutable,
        UpdatableVolume,
        RandomProvider,
        PhysicsAwareMutableBlockVolume<P> {
    val engine: Engine
    val seed: Long
    val difficulty: Difficulty
}