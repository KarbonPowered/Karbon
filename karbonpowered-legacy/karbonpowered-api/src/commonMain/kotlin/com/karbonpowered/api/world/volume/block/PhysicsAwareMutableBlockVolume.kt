package com.karbonpowered.api.world.volume.block

import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.world.block.BlockChangeFlag
import com.karbonpowered.math.vector.IntVector3

interface PhysicsAwareMutableBlockVolume<P : PhysicsAwareMutableBlockVolume<P>> : BlockVolume.Mutable<P> {

    fun setBlock(
        position: IntVector3,
        state: BlockState,
        flag: BlockChangeFlag
    ): Boolean =
        setBlock(position.x, position.y, position.z, state, flag)

    fun setBlock(
        x: Int,
        y: Int,
        z: Int,
        state: BlockState,
        flag: BlockChangeFlag
    ): Boolean

}