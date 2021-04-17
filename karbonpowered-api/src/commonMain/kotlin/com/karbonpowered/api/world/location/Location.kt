package com.karbonpowered.api.world.location

import com.karbonpowered.api.world.World
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.math.vector.IntVector3

interface Location<W : World<W, L>, L : Location<W, L>> : DoubleVector3 {
    val blockPosition: IntVector3
    val chunkPosition: IntVector3
    val world: W?
    override val x: Double
    override val y: Double
    override val z: Double
}