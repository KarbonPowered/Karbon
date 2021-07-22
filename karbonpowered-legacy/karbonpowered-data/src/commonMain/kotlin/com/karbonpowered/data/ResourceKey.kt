package com.karbonpowered.data

import com.karbonpowered.common.hash.hashCode

interface ResourceKey : ResourceKeyed {
    val namespace: String
    val value: String

    override val resourceKey: ResourceKey get() = this
}

interface ResourceKeyed {
    val resourceKey: ResourceKey
}

data class ResourceKeyImpl(override val namespace: String, override val value: String) : ResourceKey {
    private val hashCode = hashCode(namespace, value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ResourceKeyImpl

        if (namespace != other.namespace) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int = hashCode

    override fun toString(): String = "$namespace:$value"
}

fun ResourceKey(namespace: String, value: String): ResourceKey = ResourceKeyImpl(namespace, value)