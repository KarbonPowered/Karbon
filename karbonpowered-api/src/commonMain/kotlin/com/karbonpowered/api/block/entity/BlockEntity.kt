package com.karbonpowered.api.block.entity

import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.world.location.Locatable
import com.karbonpowered.api.world.location.LocatableBlock

interface BlockEntity : Locatable {
    var isValid: Boolean
    val type: BlockEntityType
    val block: BlockState
    val locatableBlock: LocatableBlock

    fun createArchetype(): BlockEntityArchetype

    fun copy(): BlockEntity
}