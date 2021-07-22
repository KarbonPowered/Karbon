package com.karbonpowered.api.world.volume.block.entity

import com.karbonpowered.api.block.entity.BlockEntity
import com.karbonpowered.api.world.volume.MutableVolume
import com.karbonpowered.api.world.volume.block.BlockVolume
import com.karbonpowered.api.world.volume.sequence.SequenceOptions
import com.karbonpowered.api.world.volume.sequence.VolumeSequence
import com.karbonpowered.math.vector.IntVector3

interface BlockEntityVolume : BlockVolume {
    val blockEntities: Collection<BlockEntity>

    fun blockEntity(x: Int, y: Int, z: Int): BlockEntity?
    fun blockEntity(position: IntVector3): BlockEntity? = blockEntity(position.x, position.y, position.z)

    interface Sequence<T : Sequence<T>> : BlockEntityVolume {
        fun blockEntitySequence(
            min: IntVector3,
            max: IntVector3,
            options: SequenceOptions
        ): VolumeSequence<T, BlockEntity>
    }

    interface Mutable<M : Mutable<M>> : Sequence<M>, BlockVolume.Mutable<M>, MutableVolume {
        fun addBlockEntity(x: Int, y: Int, z: Int, blockEntity: BlockEntity)
        fun addBlockEntity(position: IntVector3, blockEntity: BlockEntity) =
            addBlockEntity(position.x, position.y, position.z, blockEntity)

        fun removeBlockEntity(x: Int, y: Int, z: Int)
        fun removeBlockEntity(position: IntVector3) =
            removeBlockEntity(position.x, position.y, position.z)
    }
}