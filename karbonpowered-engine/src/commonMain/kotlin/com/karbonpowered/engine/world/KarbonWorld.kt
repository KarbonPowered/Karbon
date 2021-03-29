package com.karbonpowered.engine.world

import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.world.WorldLoadOption
import com.karbonpowered.api.world.chunk.Chunk
import com.karbonpowered.api.world.server.ServerWorld
import com.karbonpowered.component.BaseComponentOwner
import com.karbonpowered.math.vector.IntVector3
import com.karbonpowered.math.vector.intVector3Of
import com.karbonpowered.math.vector.minus
import com.karbonpowered.math.vector.plus

class KarbonWorld : BaseComponentOwner(), ServerWorld {
    override fun chunk(x: Int, y: Int, z: Int, loadOption: WorldLoadOption): Chunk<*>? {
        TODO("Not yet implemented")
    }

    override fun region(x: Int, y: Int, z: Int, loadOption: WorldLoadOption) {
        TODO("Not yet implemented")
    }

    override fun block(x: Int, y: Int, z: Int): BlockState {
        TODO("Not yet implemented")
    }

    override val blockMin: IntVector3 = intVector3Of(-30000000, 0, -30000000)
    override val blockMax: IntVector3 = intVector3Of(30000000, 256, 30000000)
    override val blockSize: IntVector3 = blockMax - blockMin + intVector3Of(1)

    override fun containsBlock(x: Int, y: Int, z: Int): Boolean {
        TODO("Not yet implemented")
    }

    fun startTick() {
        components.forEach {
            it.tick(1.0f/20.0f)
        }
    }
}