package com.karbonpowered.api.registry

import com.karbonpowered.api.ResourceKey

interface Registry<T : Any> : Iterable<RegistryEntry<T>> {
    val type: RegistryType<T>

    fun valueKey(value: T): ResourceKey

    fun findValueKey(value: T): ResourceKey?

    fun <V : T> findEntry(key: ResourceKey): RegistryEntry<V>?

    fun <V : T> findEntry(key: RegistryKey<T>): RegistryEntry<V>? = findEntry(key.location)

    fun <V : T> findValue(key: ResourceKey): V?

    fun <V : T> findValue(key: RegistryKey<T>): V? = findValue(key.location)

    fun <V : T> value(key: ResourceKey): V

    fun <V : T> value(key: RegistryKey<T>): V = value(key.location)

    fun isDynamic(): Boolean

    fun <V : T> register(key: ResourceKey, value: V): RegistryEntry<V>?
}