package com.karbonpowered.api.world.location

import com.karbonpowered.api.world.World
import com.karbonpowered.math.vector.DoubleVector3
import com.karbonpowered.math.vector.IntVector3
import com.karbonpowered.math.vector.doubleVector3of
import com.karbonpowered.math.vector.intVector3Of

interface LocationCreator<W : World<W, L>, L : Location<W, L>> {
    val world: W

    fun location(position: IntVector3): L
    fun location(x: Int, y: Int, z: Int): L = location(intVector3Of(x, y, z))

    fun location(position: DoubleVector3): L
    fun location(x: Double, y: Double, z: Double): L = location(doubleVector3of(x, y, z))
}