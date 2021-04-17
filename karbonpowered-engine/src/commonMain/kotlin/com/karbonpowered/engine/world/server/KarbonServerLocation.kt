package com.karbonpowered.engine.world.server

import com.karbonpowered.api.world.server.ServerLocation
import com.karbonpowered.api.world.server.ServerWorld
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.math.vector.IntVector3

class KarbonServerLocation(
        override val world: ServerWorld?,
        override val x: Double,
        override val y: Double,
        override val z: Double
) : ServerLocation {
    override val blockPosition: IntVector3
        get() = TODO("Not yet implemented")
    override val chunkPosition: IntVector3
        get() = TODO("Not yet implemented")

    override fun add(x: Double, y: Double, z: Double): DoubleVector3 {
        TODO("Not yet implemented")
    }

    override fun sub(x: Double, y: Double, z: Double): DoubleVector3 {
        TODO("Not yet implemented")
    }

    override fun mul(x: Double, y: Double, z: Double): DoubleVector3 {
        TODO("Not yet implemented")
    }

    override fun div(x: Double, y: Double, z: Double): DoubleVector3 {
        TODO("Not yet implemented")
    }
}