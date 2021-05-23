package com.karbonpowered.api.item.inventory

import com.karbonpowered.api.block.BlockType
import com.karbonpowered.api.item.ItemType
import com.karbonpowered.text.Text

interface ItemStack {
    val type: ItemType
    var quantity: Int
    val maxStackQuantity: Int
    fun createSnapshot(): ItemStackSnapshot
    fun isEmpty(): Boolean
    fun isNotEmpty(): Boolean = !isEmpty()
    val burnTime: Int
    val canHarvest: Set<BlockType>?
    val containerItem: ItemType?
    var displayName: Text?
    var customModelData: Int?
    var customName: Text?
    var unbreakable: Boolean
    var lore: List<Text>?
}