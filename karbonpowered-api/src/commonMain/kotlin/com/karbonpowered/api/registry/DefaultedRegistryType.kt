package com.karbonpowered.api.registry

import com.karbonpowered.api.Identifier

interface DefaultedRegistryType<T> : RegistryType<T>, Function0<T> {
    val defaultHolder: () -> RegistryHolder

    override fun invoke(): T

    fun find(): Registry<T>?

    fun defaultReferenced(key: Identifier): DefaultedRegistryReference<T> =
            RegistryKey(this, key).asDefaultedReference(defaultHolder)
}