package com.karbonpowered.api.registry

import com.karbonpowered.api.ResourceKey

/**
 * An entry within a [Registry].
 *
 * @param T The type of the registry
 */
interface RegistryEntry<T : Any> {
    /**
     * Gets the [ResourceKey] this entry exists under in a [Registry].
     */
    val key: ResourceKey

    /**
     * Gets the [T] value this entry points to.
     */
    val value: T

    fun asReference(): RegistryReference<T>
}