package com.karbonpowered.core.entity.living.player

import com.karbonpowered.api.entity.living.player.HandType
import com.karbonpowered.data.ResourceKey
import com.karbonpowered.data.ResourceKeyImpl

internal data class VanillaHandType(
    val key: ResourceKey
) : HandType {
    override fun toString(): String = "HandType($key)"
}

object HandTypes {
    val MAIN_HAND by handType("main_hand")
    val OFF_HAND by handType("off_hand")

    private fun handType(name: String): Lazy<HandType> = lazy {
        VanillaHandType(ResourceKeyImpl("minecraft", name))
    }
}