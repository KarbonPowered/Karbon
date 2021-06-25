package com.karbonpowered.core.entity.living.player

import com.karbonpowered.api.entity.living.player.SkinPart
import com.karbonpowered.data.ResourceKey
import com.karbonpowered.data.ResourceKeyImpl

internal data class VanillaSkinPart(
    val key: ResourceKey
) : SkinPart {
    override fun toString(): String = "SkinPart($key)"
}

object SkinParts {
    val CAPE by skinPart("cape")
    val JACKET by skinPart("jacket")
    val LEFT_SLEEVE by skinPart("left_sleeve")
    val RIGHT_SLEEVE by skinPart("right_sleeve")
    val HAT by skinPart("hat")
    val LEFT_PANTS_LEG by skinPart("left_pants")
    val RIGHT_PANTS_LEG by skinPart("right_pants")

    private fun skinPart(name: String): Lazy<SkinPart> = lazy {
        VanillaSkinPart(ResourceKeyImpl("minecraft", name))
    }
}