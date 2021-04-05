package com.karbonpowered.api.registry

import com.karbonpowered.api.Karbon
import kotlin.reflect.KClass

interface FactoryProvider {
    operator fun <T : Any> get(kClass: KClass<T>): T
}

inline fun <reified T : Any> factory(): T = Karbon.game.factoryProvider[T::class]