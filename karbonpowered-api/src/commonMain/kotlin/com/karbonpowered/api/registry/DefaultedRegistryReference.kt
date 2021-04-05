package com.karbonpowered.api.registry

interface DefaultedRegistryReference<T> : RegistryReference<T>, Function0<T> {
    val defaultHolder: () -> RegistryHolder

    override fun invoke(): T

    fun find(): T?
}