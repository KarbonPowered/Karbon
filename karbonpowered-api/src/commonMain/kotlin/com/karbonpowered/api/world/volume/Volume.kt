package com.karbonpowered.api.world.volume

import com.karbonpowered.math.vector.IntVector3

interface Volume {
    val blockMin: IntVector3
    val blockMax: IntVector3
    val blockSize: IntVector3

    fun containsBlock(position: IntVector3): Boolean = containsBlock(position.x, position.y, position.z)
    fun containsBlock(x: Int, y: Int, z: Int): Boolean
}

interface MutableVolume : Volume