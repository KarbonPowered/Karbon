package com.karbonpowered.api.registry

import kotlin.reflect.KProperty

interface DefaultedRegistryReference<T : Any> : RegistryReference<T>, Function0<T> {
    val defaultHolder: () -> RegistryHolder

    override fun invoke(): T

    fun find(): T?

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = invoke()
}