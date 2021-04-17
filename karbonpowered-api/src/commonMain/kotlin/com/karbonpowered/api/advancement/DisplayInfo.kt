package com.karbonpowered.api.advancement

import com.karbonpowered.api.item.inventory.ItemStackSnapshot
import com.karbonpowered.text.Text

interface DisplayInfo {
    val type: AdvancementType
    val description: Text
    val icon: ItemStackSnapshot
    val title: Text
    val showToast: Boolean
    val announceToChat: Boolean
    val hidden: Boolean
}