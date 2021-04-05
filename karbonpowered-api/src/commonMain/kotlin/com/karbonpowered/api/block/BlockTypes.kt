package com.karbonpowered.api.block

import com.karbonpowered.api.Identifier
import com.karbonpowered.api.Karbon
import com.karbonpowered.api.registry.DefaultedRegistryReference
import com.karbonpowered.api.registry.RegistryKey
import com.karbonpowered.api.registry.RegistryTypes

object BlockTypes {
    val AIR = key(Identifier.minecraft("air"))

    private fun key(identifier: Identifier): DefaultedRegistryReference<BlockType> =
            RegistryKey(RegistryTypes.BLOCK_TYPE, identifier).asDefaultedReference { Karbon.game.registries }
}