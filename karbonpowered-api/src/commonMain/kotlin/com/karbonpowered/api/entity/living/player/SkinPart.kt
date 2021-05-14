package com.karbonpowered.api.entity.living.player

import com.karbonpowered.api.registry.DefaultedRegistryValue

interface SkinPart : DefaultedRegistryValue

object SkinParts {
    val CAPE = object : SkinPart {}
    val HAT = object : SkinPart {}
    val JACKET = object : SkinPart {}
    val LEFT_PANTS_LEG = object : SkinPart {}
    val LEFT_SLEEVE = object : SkinPart {}
    val RIGHT_PANTS_LEG = object : SkinPart {}
    val RIGHT_SLEEVE = object : SkinPart {}
}