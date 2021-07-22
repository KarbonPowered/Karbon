package com.karbonpowered.api.registry

import com.karbonpowered.data.ResourceKey

/**
 * A holder of [Registry].
 */
interface RegistryHolder {
    /**
     * Gets the [Registry] by a [RegistryType] key.
     *
     * Great care needs to be made in calling this method with any uncertainty as to
     * if the key will exist in the holder. Should the key lack a value, a
     * [ValueNotFoundException] will be thrown. Therefore, it is advised to call
     * [findRegistry] instead.
     *
     * @param type The type
     * @param T The type
     * @return The registry
     */
    fun <T : Any> registry(type: RegistryType<T>): Registry<T>

    /**
     * Gets the [Registry] by a [RegistryType], if found.
     *
     * @param type The type
     * @param T The type
     * @return The registry or null
     */
    fun <T : Any> findRegistry(type: RegistryType<T>): Registry<T>?

    /**
     * Gets a [Sequence] of the [Registry] registries in this holder within a root.
     *
     * @param root The root to stream registries of
     * @return The stream
     */
    fun sequence(root: ResourceKey): Sequence<Registry<*>>
}