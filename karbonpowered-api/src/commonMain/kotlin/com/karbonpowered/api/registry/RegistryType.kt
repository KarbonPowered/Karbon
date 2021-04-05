package com.karbonpowered.api.registry

import com.karbonpowered.api.Identifier

interface RegistryType<T> {
    val root: Identifier
    val location: Identifier

    fun keyFor(holder: RegistryHolder, value: T): Identifier = holder.registry(this).valueKey(value)

    fun referenced(key: Identifier): RegistryReference<T> = RegistryKey(this, key).asReference()

    fun <V : T> asDefaultedType(defaultHolder: () -> RegistryHolder): DefaultedRegistryType<V>

    interface Factory {
        fun <T> create(root: Identifier, location: Identifier): RegistryType<T>
    }

    companion object {
        fun <T> of(root: Identifier, location: Identifier): RegistryType<T> = factory<Factory>().create(root, location)
    }
}

inline fun <T> RegistryType(root: Identifier, location: Identifier): RegistryType<T> = RegistryType.of(root, location)