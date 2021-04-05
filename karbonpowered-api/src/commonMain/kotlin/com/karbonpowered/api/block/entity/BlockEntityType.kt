package com.karbonpowered.api.block.entity

import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.registry.DefaultedRegistryValue

interface BlockEntityType : DefaultedRegistryValue {
    fun isValidBlock(block: BlockState): Boolean
}