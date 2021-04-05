package com.karbonpowered.api.world.palette

import com.karbonpowered.api.Identifier
import com.karbonpowered.api.Karbon
import com.karbonpowered.api.block.BlockState
import com.karbonpowered.api.block.BlockType
import com.karbonpowered.api.registry.*
import com.karbonpowered.api.world.biome.Biome

interface PaletteType<T, R> {
    val resolver: (String, Registry<R>) -> T?
    val stringfier: (Registry<R>, T) -> String

    fun create(holder: RegistryHolder, registryType: RegistryType<R>): Palette<T, R>

    companion object

    interface Builder<T, R> : com.karbonpowered.common.builder.Builder<PaletteType<T, R>, Builder<T, R>> {
        var resolver: (String, Registry<R>) -> T?
        var stringfier: (Registry<R>, T) -> String

        fun resolver(resolver: (String, Registry<R>) -> T?) = apply {
            this.resolver = resolver
        }

        fun stringfier(stringfier: (Registry<R>, T) -> String) = apply {
            this.stringfier = stringfier
        }
    }
}

object PaletteTypes {
    val BIOME_PALLETE = key<Biome, Biome>(Identifier.karbon("biome_palette"))
    val BLOCK_STATE_PALETTE = key<BlockState, BlockType>(Identifier.karbon("block_state_palette"))

    private fun <T, R> key(location: Identifier): DefaultedRegistryReference<PaletteType<T, R>> =
            RegistryKey(RegistryTypes.PALETTE_TYPE, location).asDefaultedReference { Karbon.game.registries }
}