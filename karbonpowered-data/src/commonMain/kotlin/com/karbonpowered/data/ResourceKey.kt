package com.karbonpowered.data

interface ResourceKey : ResourceKeyed {
    val namespace: String
    val value: String

    override val resourceKey: ResourceKey get() = this
}

interface ResourceKeyed {
    val resourceKey: ResourceKey
}

data class ResourceKeyImpl(override val namespace: String, override val value: String) : ResourceKey {
    override fun toString(): String = "$namespace:$value"
}