package com.karbonpowered.api.world.volume.block

import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.block.BlockType
import com.karbonpowered.api.fluid.FluidState
import com.karbonpowered.api.registry.RegistryReference
import com.karbonpowered.api.world.palette.Palette
import com.karbonpowered.api.world.volume.MutableVolume
import com.karbonpowered.api.world.volume.Volume
import com.karbonpowered.api.world.volume.sequence.SequenceOptions
import com.karbonpowered.api.world.volume.sequence.VolumeSequence
import com.karbonpowered.math.vector.IntVector2
import com.karbonpowered.math.vector.IntVector3
import com.karbonpowered.math.vector.intVector3Of

interface BlockVolume : Volume {
    fun block(x: Int, y: Int, z: Int): BlockState
    fun block(position: IntVector3): BlockState = block(position.x, position.y, position.z)

    fun fluid(x: Int, y: Int, z: Int): FluidState
    fun fluid(position: IntVector3): FluidState = fluid(position.x, position.y, position.z)

    /**
     * Get the y value of the highest block that sunlight can reach in the given
     * column.
     *
     *
     * This method ignores all transparent blocks, providing the highest
     * opaque block.
     *
     * @param x The x column value
     * @param z The z column value
     * @return The y value of the highest opaque block
     */
    fun highestYAt(x: Int, z: Int): Int

    /**
     * Get the y value of the highest block that sunlight can reach in the given
     * column.
     *
     *
     * This method ignores all transparent blocks, providing the highest
     * opaque block.
     *
     * @param column The column value
     * @return The y value of the highest opaque block
     */
    fun highestYAt(column: IntVector2): Int {
        return this.highestYAt(column.x, column.y)
    }

    /**
     * Get the [ServerLocation] of the highest block that sunlight can reach in
     * the given column.
     *
     *
     * This method ignores all transparent blocks, providing the highest
     * opaque block.
     *
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @return The highest opaque position
     */
    fun highestPositionAt(x: Int, y: Int, z: Int): IntVector3 = intVector3Of(x, this.highestYAt(x, z), z)
    fun highestPositionAt(position: IntVector3): IntVector3 = highestPositionAt(position.x, position.y, position.z)

    interface Sequence<B : Sequence<B>> : BlockVolume {
        /**
         * Gets a [VolumeSequence]<`B, `[BlockState]>
         * from this volume such that the [min] and [max] are contained
         * within this volume.
         *
         * @param min The minimum coordinate set
         * @param max The maximum coordinate set
         * @param options The options to construct the sequence
         * @return The volume sequence
         */
        fun blockStateSequence(
            min: IntVector3,
            max: IntVector3,
            options: SequenceOptions
        ): VolumeSequence<B, BlockState>
    }

    interface Mutable<M : Mutable<M>> : MutableVolume {
        fun setBlock(x: Int, y: Int, z: Int, block: BlockState): Boolean
        fun setBlock(position: IntVector3, block: BlockState): Boolean =
            setBlock(position.x, position.y, position.z, block)

        fun removeBlock(x: Int, y: Int, z: Int): Boolean
        fun removeBlock(position: IntVector3): Boolean = removeBlock(position.x, position.y, position.z)
    }

    interface Factory {
        fun empty(
            palette: Palette<BlockState, BlockType>,
            defaultState: RegistryReference<BlockType>,
            min: IntVector3,
            max: IntVector3
        ): Mutable<*>

        fun copyFromRange(existing: Sequence<*>, newMin: IntVector3, newMax: IntVector3): Mutable<*>

        fun copy(existing: Sequence<*>): Mutable<*>

        fun immutableOf(existing: Sequence<*>): BlockVolume = existing

        fun immutableOf(existing: Sequence<*>, newMin: IntVector3, newMax: IntVector3): BlockVolume
    }
}
