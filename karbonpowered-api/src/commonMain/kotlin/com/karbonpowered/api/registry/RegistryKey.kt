package com.karbonpowered.api.registry

import com.karbonpowered.data.ResourceKey

interface RegistryKey<T : Any> {
    val registry: RegistryType<T>
    val location: ResourceKey

    fun asReference(): RegistryReference<T>

    fun <V : T> asDefaultedReference(defaultHolder: () -> RegistryHolder): DefaultedRegistryReference<V>

    companion object {
        lateinit var factory: (RegistryType<*>, ResourceKey) -> RegistryKey<*>

        operator fun <T : Any> invoke(registry: RegistryType<T>, location: ResourceKey): RegistryKey<T> = factory(registry,location) as RegistryKey<T>
    }
}