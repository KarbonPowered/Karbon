package com.karbonpowered.api.world.palette

import com.karbonpowered.api.registry.RegistryHolder
import com.karbonpowered.api.registry.RegistryType

interface PaletteReference<T : Any, R : Any> {
    val registry: RegistryType<R>

    val value: String

    fun resolve(holder: RegistryHolder, type: PaletteType<T, R>): T? =
        type.resolver(value, holder.registry(registry))

    interface Factory {
        fun <T : Any, R : Any> create(type: RegistryType<R>, value: String): PaletteReference<T, R>
    }
}