package com.karbonpowered.api.world.volume.game

import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.entity.Entity
import com.karbonpowered.api.world.block.BlockChangeFlag
import com.karbonpowered.api.world.block.BlockChangeFlags
import com.karbonpowered.math.vector.IntVector3

interface MutableGameVolume {
    fun setBlock(
        position: IntVector3,
        state: BlockState,
        flag: BlockChangeFlag = BlockChangeFlags.DEFAULT_PLACEMENT
    ): Boolean =
        setBlock(position.x, position.y, position.z, state, flag)

    fun setBlock(
        x: Int,
        y: Int,
        z: Int,
        state: BlockState,
        flag: BlockChangeFlag = BlockChangeFlags.DEFAULT_PLACEMENT
    ): Boolean

    fun spawnEntity(entity: Entity<*>): Boolean

    fun removeBlock(pos: IntVector3): Boolean = removeBlock(pos.x, pos.y, pos.z)
    fun removeBlock(x: Int, y: Int, z: Int): Boolean

    fun destroyBlock(pos: IntVector3, performDrops: Boolean): Boolean
}