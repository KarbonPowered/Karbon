package com.karbonpowered.api.world.volume

import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.block.BlockType
import com.karbonpowered.math.vector.IntVector3

interface BlockVolume : Volume {
    fun block(vector: IntVector3): BlockState = block(vector.x, vector.y, vector.z)
    fun block(x: Int, y: Int, z: Int): BlockState
}

interface MutableBlockVolume<M : MutableBlockVolume<M>> : MutableVolume {
    fun setBlock(position: IntVector3, blockState: BlockState): Boolean = setBlock(position.x, position.y,position.z,blockState)
    fun setBlock(x: Int, y: Int, z: Int, blockState: BlockState): Boolean

    fun removeBlock(position: IntVector3): Boolean = removeBlock(position.x,position.y,position.z)
    fun removeBlock(x: Int, y: Int, z: Int): Boolean
}