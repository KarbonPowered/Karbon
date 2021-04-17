package com.karbonpowered.api.registry

import com.karbonpowered.api.ResourceKey
import kotlin.reflect.KProperty

interface DefaultedRegistryType<T : Any> : RegistryType<T> {
    val defaultHolder: () -> RegistryHolder

    fun get(): Registry<T>

    fun find(): Registry<T>?

    fun defaultReferenced(key: ResourceKey): DefaultedRegistryReference<T> =
            RegistryKey(this, key).asDefaultedReference(defaultHolder)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Registry<T> = get()

    operator fun invoke(): Registry<T> = get()
}