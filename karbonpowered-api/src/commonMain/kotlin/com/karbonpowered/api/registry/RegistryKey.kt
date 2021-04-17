package com.karbonpowered.api.registry

import com.karbonpowered.api.ResourceKey

interface RegistryKey<T : Any> {
    val registry: RegistryType<T>
    val location: ResourceKey

    fun asReference(): RegistryReference<T>

    fun <V : T> asDefaultedReference(defaultHolder: () -> RegistryHolder): DefaultedRegistryReference<V>

    companion object {
        fun <T : Any> of(registry: RegistryType<T>, location: ResourceKey): RegistryKey<T> =
                factory<Factory>().create(registry, location)
    }

    interface Factory {
        fun <T : Any> create(registry: RegistryType<T>, location: ResourceKey): RegistryKey<T>
    }
}

inline fun <T : Any> RegistryKey(registry: RegistryType<T>, location: ResourceKey): RegistryKey<T> =
        RegistryKey.of(registry, location)