package com.karbonpowered.api.world.volume.game

import com.karbonpowered.api.block.BlockType
import com.karbonpowered.api.fluid.FluidType
import com.karbonpowered.api.scheduler.ScheduledUpdateList
import com.karbonpowered.api.world.volume.block.BlockVolume

interface UpdatableVolume : BlockVolume {
    val scheduledBlockUpdates: ScheduledUpdateList<BlockType>
    val scheduledFluidUpdates: ScheduledUpdateList<FluidType>
}