package com.karbonpowered.api.registry

import com.karbonpowered.api.ResourceKey

interface RegistryType<T : Any> {
    val root: ResourceKey
    val location: ResourceKey

    fun keyFor(holder: RegistryHolder, value: T): ResourceKey = holder.registry(this).valueKey(value)

    fun referenced(key: ResourceKey): RegistryReference<T> = RegistryKey(this, key).asReference()

    fun <V : T> asDefaultedType(defaultHolder: () -> RegistryHolder): DefaultedRegistryType<V>

    interface Factory {
        fun <T : Any> create(root: ResourceKey, location: ResourceKey): RegistryType<T>
    }

    companion object {
        fun <T : Any> of(root: ResourceKey, location: ResourceKey): RegistryType<T> =
                factory<Factory>().create(root, location)
    }
}

inline fun <T : Any> RegistryType(root: ResourceKey, location: ResourceKey): RegistryType<T> =
        RegistryType.of(root, location)