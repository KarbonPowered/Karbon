package com.karbonpowered.api.datapack

import kotlin.reflect.KClass

interface DataPackType<T : Any> {
    val type: KClass<T>

    val persistent: Boolean
}