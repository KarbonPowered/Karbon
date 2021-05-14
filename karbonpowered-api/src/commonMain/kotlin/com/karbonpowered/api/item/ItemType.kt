package com.karbonpowered.api.item

import com.karbonpowered.api.block.BlockType
import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.text.TextRepresentable

interface ItemType : DefaultedRegistryValue, TextRepresentable {
    val blockType: BlockType?
    val container: ItemType?
    val maxStackQuantity: Int
}