package com.karbonpowered.api.world.volume.game

import com.karbonpowered.api.block.BlockState
import com.karbonpowered.math.vector.IntVector3

interface GenerationVolume : HeightAwareVolume {
    fun hasBlockState(position: IntVector3, predicate: (BlockState) -> Boolean = { true }): Boolean =
            hasBlockState(position.x, position.y, position.z, predicate)

    fun hasBlockState(x: Int, y: Int, z: Int, predicate: (BlockState) -> Boolean = { true }): Boolean

    interface Mutable : GenerationVolume
}