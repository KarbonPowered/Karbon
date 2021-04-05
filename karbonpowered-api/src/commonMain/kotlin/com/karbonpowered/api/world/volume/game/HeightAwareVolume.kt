package com.karbonpowered.api.world.volume.game

import com.karbonpowered.api.world.height.HeightType
import com.karbonpowered.math.vector.IntVector3

interface HeightAwareVolume {
    fun height(type: HeightType, position: IntVector3): IntVector3 =
            IntVector3(position.x, height(type, position.x, position.z), position.z)

    fun height(type: HeightType, x: Int, z: Int): Int
}