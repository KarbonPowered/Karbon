package com.karbonpowered.api.world.volume

import com.karbonpowered.api.world.WorldLoadOption
import com.karbonpowered.api.world.chunk.ProtoChunk
import com.karbonpowered.api.world.volume.block.BlockVolume
import com.karbonpowered.math.vector.IntVector3

interface ChunkVolume : BlockVolume {
    fun chunk(chunkPosition: IntVector3, loadOption: WorldLoadOption = WorldLoadOption.LOAD_GEN): ProtoChunk<*>? =
            chunk(chunkPosition.x, chunkPosition.y, chunkPosition.z, loadOption)

    fun chunk(x: Int, y: Int, z: Int, loadOption: WorldLoadOption = WorldLoadOption.LOAD_GEN): ProtoChunk<*>?

    fun chunkAtBlock(blockPosition: IntVector3, loadOption: WorldLoadOption = WorldLoadOption.GEN_ONLY): ProtoChunk<*>? =
            chunkAtBlock(blockPosition.x, blockPosition.y, blockPosition.z, loadOption)

    fun chunkAtBlock(x: Int, y: Int, z: Int, loadOption: WorldLoadOption = WorldLoadOption.GEN_ONLY): ProtoChunk<*>?
}