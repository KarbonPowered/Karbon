package com.karbonpowered.api.world.location

import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.registry.builder
import com.karbonpowered.api.world.server.ServerLocation
import com.karbonpowered.api.world.server.ServerWorld
import com.karbonpowered.math.vector.IntVector3

interface LocatableBlock : Locatable {
    val blockState: BlockState

    companion object

    interface Builder : com.karbonpowered.common.builder.Builder<LocatableBlock, Builder> {
        var state: BlockState
        var location: ServerLocation
        var position: IntVector3
        var x: Int
        var y: Int
        var z: Int
        var world: ServerWorld

        fun state(blockState: BlockState) = apply {
            this.state = blockState
        }

        fun location(location: ServerLocation) = apply {
            this.location = location
        }

        fun position(position: IntVector3) = apply {
            this.position = position
        }

        fun position(x: Int, y: Int, z: Int) = apply {
            this.x = x
            this.y = y
            this.z = z
        }

        fun world(world: ServerWorld) = apply {
            this.world
        }
    }
}

operator fun LocatableBlock.Companion.invoke(builder: LocatableBlock.Builder.() -> Unit): LocatableBlock =
        builder<LocatableBlock.Builder>().apply(builder).build()