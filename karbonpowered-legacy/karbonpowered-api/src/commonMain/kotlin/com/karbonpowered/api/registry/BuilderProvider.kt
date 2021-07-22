package com.karbonpowered.api.registry

import com.karbonpowered.common.builder.Builder
import kotlin.reflect.KClass

interface BuilderProvider {
    operator fun <B : Builder<*, in B>> get(builderClass: KClass<in B>): B
}