package com.karbonpowered.api.registry

import com.karbonpowered.api.Identifier

interface Registry<T> : Iterable<RegistryEntry<T>> {
    val type: RegistryType<T>

    fun valueKey(value: T): Identifier

    fun findValueKey(value: T): Identifier?

    fun <V : T> findEntry(key: Identifier): RegistryEntry<V>?

    fun <V : T> findEntry(key: RegistryKey<T>): RegistryEntry<V>? = findEntry(key.location)

    fun <V : T> findValue(key: Identifier): V?

    fun <V : T> findValue(key: RegistryKey<T>): V? = findValue(key.location)

    fun <V : T> value(key: Identifier): V

    fun <V : T> value(key: RegistryKey<T>): V = value(key.location)

    fun isDynamic(): Boolean

    fun <V : T> register(key: Identifier, value: V): RegistryEntry<V>?
}