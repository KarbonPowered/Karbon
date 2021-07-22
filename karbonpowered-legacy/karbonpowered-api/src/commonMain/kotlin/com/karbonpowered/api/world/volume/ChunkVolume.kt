package com.karbonpowered.api.world.volume

import com.karbonpowered.api.world.WorldLoadOption
import com.karbonpowered.api.world.volume.block.BlockVolume

interface ChunkVolume : BlockVolume {
    fun chunk(x: Int, y: Int, z: Int, loadOnly: WorldLoadOption)
}