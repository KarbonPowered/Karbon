package com.karbonpowered.api.world

import com.karbonpowered.api.Engine
import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.world.block.BlockChangeFlag
import com.karbonpowered.api.world.difficulty.Difficulty
import com.karbonpowered.api.world.volume.biome.BiomeVolume
import com.karbonpowered.api.world.volume.block.BlockVolume
import com.karbonpowered.api.world.volume.block.PhysicsAwareMutableBlockVolume
import com.karbonpowered.api.world.volume.block.entity.BlockEntityVolume
import com.karbonpowered.api.world.volume.entity.EntityVolume
import com.karbonpowered.api.world.volume.game.GenerationVolume
import com.karbonpowered.api.world.volume.game.LocationBaseDataHolder
import com.karbonpowered.api.world.volume.game.MutableGameVolume
import com.karbonpowered.api.world.volume.game.UpdatableVolume
import com.karbonpowered.common.RandomProvider
import com.karbonpowered.math.vector.IntVector3

interface ProtoWorld<P : ProtoWorld<P>> :
    BiomeVolume.Mutable<P>,
    BlockVolume.Mutable<P>,
    EntityVolume.Mutable<P>,
    BlockEntityVolume.Mutable<P>,
    GenerationVolume.Mutable,
    LocationBaseDataHolder.Mutable,
    UpdatableVolume,
    RandomProvider,
    PhysicsAwareMutableBlockVolume<P>,
    MutableGameVolume {
    val engine: Engine
    val seed: Long
    val difficulty: Difficulty

    override fun setBlock(position: IntVector3, state: BlockState, flag: BlockChangeFlag): Boolean =
        setBlock(position.x, position.y, position.z, state, flag)

    override fun setBlock(x: Int, y: Int, z: Int, state: BlockState, flag: BlockChangeFlag): Boolean

    override fun removeBlock(position: IntVector3): Boolean =
        removeBlock(position.x, position.y, position.z)

    override fun removeBlock(x: Int, y: Int, z: Int): Boolean
}