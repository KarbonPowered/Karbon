package com.karbonpowered.api.item.inventory

import com.karbonpowered.api.item.ItemType

interface ItemStackSnapshot {
    val type: ItemType
    val quantity: Int
    fun isEmpty(): Boolean
    fun isNotEmpty(): Boolean = !isEmpty()

    fun createStack(): ItemStack
}