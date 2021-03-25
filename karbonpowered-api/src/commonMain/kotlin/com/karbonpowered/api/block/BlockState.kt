package com.karbonpowered.api.block

import com.karbonpowered.api.state.State

interface BlockState : State<BlockState> {
    val type: BlockType
}