package com.karbonpowered.api.registry

import com.karbonpowered.api.Karbon
import com.karbonpowered.common.builder.Builder
import kotlin.reflect.KClass

interface BuilderProvider {
    operator fun <T : Builder<*, in T>> get(builderClass: KClass<in T>): T
}

inline fun <reified T : Builder<*, in T>> builder(): T = Karbon.game.builderProvider[T::class]