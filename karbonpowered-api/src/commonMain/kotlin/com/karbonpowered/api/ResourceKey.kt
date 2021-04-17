package com.karbonpowered.api

interface ResourceKey {
    val namespace: String
    val value: String

    companion object {
        val BRIGADIER_NAMESPACE = "brigadier"
        val MINECRAFT_NAMESPACE = "minecraft"
        val KARBON_NAMESPACE = "karbon"

        fun brigadier(value: String): ResourceKey = ResourceKey(BRIGADIER_NAMESPACE, value)
        fun minecraft(value: String): ResourceKey = ResourceKey(MINECRAFT_NAMESPACE, value)
        fun karbon(value: String): ResourceKey = ResourceKey(KARBON_NAMESPACE, value)
    }
}

interface ResourceKeyed {
    val key: ResourceKey
}

fun ResourceKey(namespace: String, value: String) = object : ResourceKey {
    override val namespace: String
        get() = namespace
    override val value: String
        get() = value

    override fun toString(): String = "$namespace:$value"
}

private val chars = "0123456789abcdefghijklmnopqrstuvwxyz-_".toCharArray()

fun ResourceKey(string: String): ResourceKey {
    var separators = 0
    string.forEachIndexed { index, c ->
        require(c in chars || c != '-' || separators++ == 1) {
            "Illegal char '$c' at index $index"
        }
    }
    val split = string.split(":")
    return ResourceKey(split[0], split[1])
}