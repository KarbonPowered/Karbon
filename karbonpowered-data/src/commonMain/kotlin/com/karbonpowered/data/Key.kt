package com.karbonpowered.data

import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

interface Key<V : Value<*>> : ResourceKeyed {
    val valueType: KClassifier
    val elementType: KClassifier
    val comparator: Comparator<*>
    val elementIncludesTester: (Any, Any) -> Boolean

    interface Builder<E, V : Value<E>> : ResourceKeyedBuilder<Key<V>, Builder<E, V>> {
        var comparator: Comparator<out E>
        var includesTester: (E, E) -> Boolean

        fun <T : Any> elementType(type: KClass<T>): Builder<T, Value<T>>
    }
}

class TypeToken<T> {
    fun extract() {
    }
}