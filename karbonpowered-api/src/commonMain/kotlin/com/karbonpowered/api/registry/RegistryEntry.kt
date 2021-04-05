package com.karbonpowered.api.registry

import com.karbonpowered.api.Identifier

/**
 * An entry within a [Registry].
 *
 * @param T The type of the registry
 */
interface RegistryEntry<T> {
    /**
     * Gets the [Identifier] this entry exists under in a [Registry].
     */
    val key: Identifier

    /**
     * Gets the [T] value this entry points to.
     */
    val value: T

    fun asReference(): RegistryReference<T>
}