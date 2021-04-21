package com.karbonpowered.api.registry

interface DefaultedRegistryValue {
    fun <T : Any> key(type: DefaultedRegistryType<T>) = type.get().valueKey(this as T)

    fun <T : Any> findKey(type: DefaultedRegistryType<T>) = type.find()?.findValueKey(this as T)

    fun <T : Any> asDefaultedReference(type: DefaultedRegistryType<T>): DefaultedRegistryReference<T> =
        RegistryKey(type, key(type)).asDefaultedReference(type.defaultHolder)
}