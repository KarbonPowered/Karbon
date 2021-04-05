package com.karbonpowered.api.registry

import com.karbonpowered.api.Identifier

interface RegistryKey<T> {
    val registry: Registry<T>
    val location: Identifier

    fun asReference(): RegistryReference<T>

    fun <V : T> asDefaultedReference(defaultHolder: () -> RegistryHolder): DefaultedRegistryReference<V>

    companion object {
        fun <T> of(registry: RegistryType<T>, location: Identifier): RegistryKey<T> = factory<Factory>().create(registry, location)
    }

    interface Factory {
        fun <T> create(registry: RegistryType<T>, location: Identifier): RegistryKey<T>
    }
}

inline fun <T> RegistryKey(registry: RegistryType<T>, location: Identifier): RegistryKey<T> = RegistryKey.of(registry, location)