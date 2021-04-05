package com.karbonpowered.api.fluid

import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.state.State

/**
 * Represents a particular "state" that can exist at a [ServerLocation] with
 * a particular [BlockType] and various values defining
 * the information for the "block". Note that normally, there may exist only
 * a single instance of a particular [FluidState] as they are immutable,
 * a particular instance may be cached for various uses.
 */
interface FluidState : State<FluidState> {
    /**
     * Gets the [BlockState] that best represents this [FluidState].
     *
     * The type does not include location based information such as tanks
     * or inventories. This is simply a block state that can be used for volumes
     * such as [Schematic]s or [World]s that are serialized and
     * deserialized.
     *
     * @return The type of block
     */
    val block: BlockState

    /**
     * Gets the parent [FluidType] that this state is based on. Much
     * like [BlockType] versus [BlockState]s, there can be
     * many to one relationships between the two.
     *
     * @return The fluid type
     */
    val type: FluidType

    fun isEmpty(): Boolean

    interface Builder : State.Builder<FluidState, Builder> {
        var fluid: FluidType

        fun fluid(fluidType: FluidType): Builder
    }
}