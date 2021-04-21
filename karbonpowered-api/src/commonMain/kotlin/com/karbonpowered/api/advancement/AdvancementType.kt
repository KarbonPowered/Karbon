package com.karbonpowered.api.advancement

import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.text.TextColor

interface AdvancementType : DefaultedRegistryValue {
    val textColor: TextColor
}