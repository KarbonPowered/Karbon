package com.karbonpowered.api.world.volume

import com.karbonpowered.api.world.WorldLoadOption
import com.karbonpowered.api.world.chunk.Chunk
import com.karbonpowered.math.vector.IntVector3

interface ChunkVolume : BlockVolume {
    fun chunk(vector: IntVector3, loadOption: WorldLoadOption = WorldLoadOption.LOAD_GEN): Chunk<*>? =
        chunk(vector.x, vector.y, vector.z, loadOption)

    fun chunk(x: Int, y: Int, z: Int, loadOption: WorldLoadOption = WorldLoadOption.LOAD_GEN): Chunk<*>?
}