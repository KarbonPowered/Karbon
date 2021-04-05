package com.karbonpowered.api.world.server

import com.karbonpowered.api.registry.factory
import com.karbonpowered.api.world.location.LocatableBlock
import com.karbonpowered.api.world.location.LocationCreator
import com.karbonpowered.math.vector.IntVector3

interface ServerLocationCreator : LocationCreator<ServerWorld, ServerLocation> {
    fun locatableBlock(position: IntVector3): LocatableBlock = locatableBlock(position.x, position.y, position.z)

    fun locatableBlock(x: Int, y: Int, z: Int): LocatableBlock =
            factory<Factory>().create(world, x, y, z)

    interface Factory {
        fun create(world: ServerWorld, x: Int, y: Int, z: Int): LocatableBlock
    }
}