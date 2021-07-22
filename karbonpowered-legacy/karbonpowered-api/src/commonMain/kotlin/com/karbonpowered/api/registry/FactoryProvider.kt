package com.karbonpowered.api.registry

import kotlin.reflect.KClass

interface FactoryProvider {
    operator fun <T : Any> get(kClass: KClass<T>): T
}