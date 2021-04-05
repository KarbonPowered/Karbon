package com.karbonpowered.api.world.volume

import com.karbonpowered.api.world.WorldLoadOption
import com.karbonpowered.math.vector.IntVector3

interface RegionVolume : ChunkVolume {
    fun region(vector: IntVector3, loadOption: WorldLoadOption = WorldLoadOption.LOAD_GEN) =
            region(vector.x, vector.y, vector.z, loadOption)

    fun region(x: Int, y: Int, z: Int, loadOption: WorldLoadOption = WorldLoadOption.LOAD_GEN)
}