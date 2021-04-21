package com.karbonpowered.api.world.chunk

import com.karbonpowered.api.entity.Entity
import com.karbonpowered.api.world.ProtoWorld
import com.karbonpowered.api.world.volume.biome.BiomeVolume
import com.karbonpowered.api.world.volume.block.BlockVolume
import com.karbonpowered.api.world.volume.block.entity.BlockEntityVolume
import com.karbonpowered.api.world.volume.game.HeightAwareVolume
import com.karbonpowered.api.world.volume.game.LocationBaseDataHolder
import com.karbonpowered.api.world.volume.game.UpdatableVolume
import com.karbonpowered.math.vector.IntVector3

interface ProtoChunk<P : ProtoChunk<P>> :
    BlockVolume.Mutable<P>,
    BlockEntityVolume.Mutable<P>,
    BiomeVolume.Mutable<P>,
    UpdatableVolume,
    LocationBaseDataHolder.Mutable,
    HeightAwareVolume {

    val state: ChunkState
    val chunkPosition: IntVector3
    val world: ProtoWorld<*>
    val regionalDifficultyFactor: Double
    val regionalDifficultyPercentage: Double
    var inhabitedTime: Double

    fun addEntity(entity: Entity<*>)

    fun isEmpty(): Boolean
}