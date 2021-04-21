package com.karbonpowered.api.registry

import com.karbonpowered.api.ResourceKey

interface RegistryKey<T : Any> {
    val registry: RegistryType<T>
    val location: ResourceKey

    fun asReference(): RegistryReference<T>

    fun <V : T> asDefaultedReference(defaultHolder: () -> RegistryHolder): DefaultedRegistryReference<V>

    interface Factory {
        fun <T : Any> create(registry: RegistryType<T>, location: ResourceKey): RegistryKey<T>
    }
}