package com.karbonpowered.api.world.palette

import com.karbonpowered.api.registry.RegistryHolder

/**
 * Represents a mapping for types to a local identifier. Can be used for
 * mapping `Any`s to `Int` id's for storage purposes,
 * or for converting stored information to a representable format back into
 * [Any].
 *
 * @param T The type this palette will maintain
 * @param R The type of registry used to build this palette
 */
interface Palette<T : Any, R : Any> : Iterable<T> {
    val type: PaletteType<T, R>
    val highestId: Int

    /**
     * Gets the `type` represented by the given identifier from the mapping.
     *
     * @param id The identifier
     * @return The type, if found
     */
    operator fun get(id: Int): PaletteReference<T, R>?

    operator fun get(id: Int, holder: RegistryHolder): T? = get(id)?.let { reference ->
        holder.findRegistry(reference.registry)?.let { registry ->
            type.resolver(reference.value, registry)
        }
    }

    /**
     * Gets the identifier for the given type [T] if it exists within the
     * mapping.
     *
     * @param type The type
     * @return The identifier, if found
     */
    operator fun get(type: T): Int?

    fun valuesWithIds(): Sequence<Pair<T, Int>>

    fun asMutable(registryHolder: RegistryHolder): Mutable<T, R>
    fun asImmutable(): Palette<T, R> = this

    interface Mutable<M : Any, MR : Any> : Palette<M, MR> {
        /**
         * Gets the identifier for the given `type` [T] from the mapping. If the
         * `type` [T] is not yet registered in the mapping then it is registered and
         * given the next available identifier.
         *
         * @param type The type
         * @return The identifier
         */
        fun orAssign(type: M): Int

        /**
         * Removes the given `type` [T] from the mapping.
         *
         * Note that if this palette is considered a global palette, removal is not supported.
         *
         * @param type The type to remove
         * @return If the type existed in the mapping
         */
        fun remove(type: M): Boolean

        override fun asMutable(registryHolder: RegistryHolder): Mutable<M, MR> = this
    }
}