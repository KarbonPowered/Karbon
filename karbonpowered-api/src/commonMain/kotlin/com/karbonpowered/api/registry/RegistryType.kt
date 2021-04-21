package com.karbonpowered.api.registry

import com.karbonpowered.api.ResourceKey

interface RegistryType<T : Any> {
    val root: ResourceKey
    val location: ResourceKey

    fun keyFor(holder: RegistryHolder, value: T): ResourceKey = holder.registry(this).valueKey(value)

    fun <V : T> asDefaultedType(defaultHolder: () -> RegistryHolder): DefaultedRegistryType<V>

    interface Factory {
        fun <T : Any> create(root: ResourceKey, location: ResourceKey): RegistryType<T>
    }
}