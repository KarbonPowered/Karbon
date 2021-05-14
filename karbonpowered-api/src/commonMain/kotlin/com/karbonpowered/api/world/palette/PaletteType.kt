package com.karbonpowered.api.world.palette

import com.karbonpowered.api.registry.Registry
import com.karbonpowered.api.registry.RegistryHolder
import com.karbonpowered.api.registry.RegistryType

interface PaletteType<T : Any, R : Any> {
    val resolver: (String, Registry<R>) -> T?
    val stringfier: (Registry<R>, T) -> String

    fun create(holder: RegistryHolder, registryType: RegistryType<R>): Palette<T, R>

    companion object

    interface Builder<T : Any, R : Any> : com.karbonpowered.common.builder.Builder<PaletteType<T, R>, Builder<T, R>> {
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